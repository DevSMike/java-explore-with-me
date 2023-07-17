package ru.practicum.ewm.main.service.event;

import ru.practicum.dto.HitDto;
import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.EventMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.client.StatsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Override
    public EventFullDto createEvent(NewEventDto eventDto, long userId) {
        if (eventDto.getCategory() == null) {
            throw new IncorrectDataException("Field: category. Error: must not be blank. Value: null");
        }
        if (eventDto.getAnnotation() == null) {
            throw new IncorrectDataException("Field: annotation. Error: must not be blank. Value: null");
        }
        if (eventDto.getEventDate() == null) {
            throw new IncorrectDataException("Field: event date. Error: must not be blank. Value: null");
        }
        if (eventDto.getDescription() == null) {
            throw new IncorrectDataException("Field: description. Error: must not be blank. Value: null");
        }
        if (eventDto.getLocation() == null) {
            throw new IncorrectDataException("Field: location. Error: must not be blank. Value: null");
        }
        if (eventDto.getTitle() == null) {
            throw new IncorrectDataException("Field: title. Error: must not be blank. Value: null");
        }

        if (eventDto.getDescription().isBlank()) {
            throw new IncorrectDataException("Field: description. Error: must not be blank. Value: blank");
        }
        if (eventDto.getAnnotation().isBlank()) {
            throw new IncorrectDataException("Field: annotation. Error: must not be blank. Value: blank");
        }

        checkAboutEventInfo(eventDto);

        LocalDateTime eventTime = EventMapper.toDateFromString(eventDto.getEventDate());
        if (LocalDateTime.now().until(eventTime, ChronoUnit.HOURS) < 2) {
            throw new IncorrectDataException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + eventDto.getEventDate());
        }

        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }

        User initiator = findUserById(userId);
        Category category = findCategoryById(eventDto.getCategory());
        Event event = EventMapper.toEventFromNewEvent(eventDto, initiator, category, LocalDateTime.now());
        eventRepository.save(event);
        return EventMapper.toEventFullDtoFromEvent(event);
    }

    @Override
    public List<EventFullDto> getUserEvents(long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.getUserEvents(userId, page).stream()
                .map(EventMapper::toEventFullDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventById(long userId, long eventId) {
        findUserById(userId);
        return EventMapper.toEventFullDtoFromEvent(eventRepository.findEventById(userId, eventId)
                .orElseThrow(() -> new NoDataException("Event with id = " + eventId + " was not found")));
    }

    @Override
    public EventFullDto updateEventById(long userId, long eventId, NewEventDto newEvent) {
        Event eventFromDb = eventRepository.findEventById(userId, eventId)
                .orElseThrow(() -> new NoDataException("Event with id = " + eventId + " was not found"));
        Category category = null;
        if (newEvent.getCategory() != null) {
            category = findCategoryById(newEvent.getCategory());
        }
        if (eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Only pending or canceled events can be changed");
        }
        if (newEvent.getEventDate() != null && !newEvent.getEventDate().isEmpty()) {
            if (LocalDateTime.now().until(EventMapper.toDateFromString(newEvent.getEventDate()), ChronoUnit.HOURS) < 2) {
                throw new IncorrectDataException("Field: eventDate. Error: должно содержать дату, которая еще не наступила." +
                        " Value: " + newEvent.getEventDate());
            }
        }
        checkAboutEventInfo(newEvent);

        Event newMappedEvent = EventMapper.toEventUpdate(eventFromDb, newEvent, category);
        eventRepository.save(newMappedEvent);
        return EventMapper.toEventFullDtoFromEvent(newMappedEvent);
    }

    @Override
    public List<EventFullDto> findEventsBySearch(List<Long> userIds, List<Long> categoriesIds, List<String> states,
                                                 String rangeStart, String rangeEnd, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);

        List<EventState> eventStates;
        if (states != null) {
            eventStates = states.stream()
                    .map(EventState::converToEventState)
                    .collect(Collectors.toList());
        } else {
            eventStates = null;
        }

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (rangeStart != null) {
            startDate = EventMapper.toDateFromString(rangeStart);
        }

        if (rangeEnd != null) {
            endDate = EventMapper.toDateFromString(rangeEnd);
        }
        return eventRepository.findEventsBySearchWithSpec(userIds, categoriesIds, eventStates, startDate, endDate, page)
                .stream()
                .map(EventMapper::toEventFullDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(long eventId, NewEventDto event) {
        Event eventFromDb = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoDataException("Event with id = " + eventId + " was not found"));
        LocalDateTime publishedTime = LocalDateTime.now();
        if (event.getEventDate() != null && !event.getEventDate().isEmpty()) {
            if (publishedTime.until(EventMapper.toDateFromString(event.getEventDate()), ChronoUnit.HOURS) < 1) {
                throw new IncorrectDataException("Field: eventDate. Error: разница между временем должна быть не меньше 1 часа." +
                        " Value: " + event.getEventDate());
            }
        }
        if (eventFromDb.getState().equals(EventState.CANCELED)) {
            throw new ConflictDataException("Field: eventState. Error: state не должен быть CANCELED для публикации" +
                    " Value: CANCELED");
        }
        if (eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Field: eventState. Error: event уже опубликован. Value: PUBLISHED");
        }

        checkAboutEventInfo(event);

        Category category = null;
        if (event.getCategory() != null) {
            category = findCategoryById(event.getCategory());
        }
        Event newEvent = EventMapper.toEventUpdateByAdmin(eventFromDb, event, category, publishedTime);
        eventRepository.save(newEvent);
        return EventMapper.toEventFullDtoFromEvent(newEvent);
    }

    @Override
    public List<EventShortDto> findEventsByPublicSearch(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                        String rangeEnd, Boolean onlyAvailable, String sort, int from,
                                                        int size, String ip) {
        saveStat("/events", ip);

        Pageable page = PageRequest.of(from / size, size);
        LocalDateTime start;
        LocalDateTime end = null;

        if (rangeStart == null) {
            start = LocalDateTime.now();
        } else {
            start = EventMapper.toDateFromString(rangeStart);
        }

        if (rangeEnd != null) {
            end = EventMapper.toDateFromString(rangeEnd);
            if (start.isAfter(end)) {
                throw new IncorrectDataException("Field: endDate. Error: конец события не может быть в прошлом");
            }
        }

        if (onlyAvailable == null) {
            onlyAvailable = false;
        }

        List<Event> result;

        if (onlyAvailable) {
            result = eventRepository.findEventsByPublicSearchOnlyAvailableWithSpec(text, categories, paid, start, end,
                    EventState.PUBLISHED, page);
        } else {
            result = eventRepository.findEventsByPublicSearchWithSpec(text, categories, paid, start, end,
                    EventState.PUBLISHED, page);
        }

        if (sort == null) {
            sort = "EVENT_DATE";
        }

        switch (sort) {
            case "EVENT_DATE": {
                result = result.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());
                break;
            }
            case "VIEWS": {
                result = result.stream()
                        .sorted(Comparator.comparingLong(x -> -x.getViews()))
                        .collect(Collectors.toList());
                break;
            }
        }

        return result.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventByIdPublic(long eventId, String ip) {
        Event eventFromDb = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoDataException("Event with id = " + eventId + " was not found"));
        if (!eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new NoDataException("Field: eventState. Error: event еще не опубликован. Value: " + eventFromDb.getState());
        }
        saveStat("/events/" + eventId, ip);
        String body = statsClient.getStats("2020-05-05 00:00:00", "2050-05-05 00:00:00",
                Collections.singletonList("/events/" + eventId), true).getBody().toString();
        eventFromDb.setViews(Integer.parseInt(body.substring(body.lastIndexOf("=") + 1, body.length() - 2)));
        return EventMapper.toEventFullDtoFromEvent(eventFromDb);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoDataException("User with id = " + userId + " was not found"));
    }

    private Category findCategoryById(long categoryId) {
        return categoryRepository.findById((categoryId))
                .orElseThrow(() -> new NoDataException("Category with id = " + categoryId + " was not found"));
    }

    private void saveStat(String uri, String ip) {
        HitDto hit = HitDto.builder()
                .app("ewm-service")
                .timestamp(EventMapper.toStringFromDate(LocalDateTime.now()))
                .uri(uri)
                .ip(ip)
                .build();
        statsClient.createHit(hit);
    }

    private void checkAboutEventInfo(NewEventDto newEvent) {
        if (newEvent.getAnnotation() != null) {
            if (newEvent.getAnnotation().length() > 2000 || newEvent.getAnnotation().length() < 20) {
                throw new IncorrectDataException("Field: annotation. Error: incorrect length");
            }
        }
        if (newEvent.getDescription() != null) {
            if (newEvent.getDescription().length() > 7000 || newEvent.getDescription().length() < 20) {
                throw new IncorrectDataException("Field: desc. Error: incorrect length");
            }
        }
        if (newEvent.getTitle() != null) {
            if (newEvent.getTitle().length() > 120 || newEvent.getTitle().length() < 3) {
                throw new IncorrectDataException("Field: title Error: incorrect length");
            }
        }
    }
}
