package ru.practicum.ewm.main.mapper;

import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toDateFromString(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public static String toStringFromDate(LocalDateTime time) {
        return time.format(DATE_TIME_FORMATTER);
    }

    public static Event toEventFromNewEvent(NewEventDto event, User user, Category category, LocalDateTime createdOn) {
        return Event.builder()
                .initiator(user)
                .state(EventState.PENDING)
                .annotation(event.getAnnotation())
                .eventDate(toDateFromString(event.getEventDate()))
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0)
                .isPaid(event.getPaid())
                .isRequestModeration(event.getRequestModeration())
                .participantLimit(event.getParticipantLimit())
                .location(event.getLocation())
                .description(event.getDescription())
                .createdOn(createdOn)
                .category(category)
                .title(event.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDtoFromEvent(Event event) {
        UserDto user = UserMapper.toUserDto(event.getInitiator());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(event.getCategory());
        return EventFullDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .category(categoryDto)
                .initiator(user)
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(DATE_TIME_FORMATTER))
                .description(event.getDescription())
                .location(event.getLocation())
                .paid(event.getIsPaid())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(DATE_TIME_FORMATTER) : null)
                .title(event.getTitle())
                .requestModeration(event.getIsRequestModeration())
                .views(event.getViews() != null ? event.getViews() : 0)
                .build();
    }

    public static Event toEventUpdate(Event oldEvent, NewEventDto newEvent, Category newCategory) {
        EventState state = checkStateAction(newEvent);
        return Event.builder()
                .id(oldEvent.getId())
                .annotation(newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation())
                .eventDate(newEvent.getEventDate() != null ? toDateFromString(newEvent.getEventDate()) : oldEvent.getEventDate())
                .confirmedRequests(newEvent.getConfirmedRequests() != null ? newEvent.getConfirmedRequests() : oldEvent.getConfirmedRequests())
                .isPaid(newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getIsPaid())
                .isRequestModeration(newEvent.getRequestModeration() != null ? newEvent.getRequestModeration() : oldEvent.getIsRequestModeration())
                .participantLimit(newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit() : oldEvent.getParticipantLimit())
                .location(newEvent.getLocation() != null ? newEvent.getLocation() : oldEvent.getLocation())
                .description(newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription())
                .category(newCategory != null ? newCategory : oldEvent.getCategory())
                .title(newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle())
                .state(state)
                .createdOn(oldEvent.getCreatedOn())
                .initiator(oldEvent.getInitiator())
                .views(oldEvent.getViews())
                .publishedOn(null)
                .build();
    }

    public static Event toEventUpdateByAdmin(Event oldEvent, NewEventDto newEvent, Category newCategory,
                                             LocalDateTime publishedDate) {

        EventState state = checkStateAction(newEvent);

        return Event.builder()
                .id(oldEvent.getId())
                .annotation(newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation())
                .eventDate(newEvent.getEventDate() != null ? toDateFromString(newEvent.getEventDate()) : oldEvent.getEventDate())
                .confirmedRequests(newEvent.getConfirmedRequests() != null ? newEvent.getConfirmedRequests() : oldEvent.getConfirmedRequests())
                .isPaid(newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getIsPaid())
                .isRequestModeration(newEvent.getRequestModeration() != null ? newEvent.getRequestModeration() : oldEvent.getIsRequestModeration())
                .participantLimit(newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit() : oldEvent.getParticipantLimit())
                .location(newEvent.getLocation() != null ? newEvent.getLocation() : oldEvent.getLocation())
                .description(newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription())
                .category(newCategory != null ? newCategory : oldEvent.getCategory())
                .title(newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle())
                .state(state)
                .createdOn(oldEvent.getCreatedOn())
                .initiator(oldEvent.getInitiator())
                .views(oldEvent.getViews())
                .publishedOn(publishedDate)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        UserDto user = UserMapper.toUserDto(event.getInitiator());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(event.getCategory());
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .paid(event.getIsPaid())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(user)
                .build();
    }

    private static EventState checkStateAction(NewEventDto newEvent) {
        EventState state;
        if (newEvent.getStateAction() != null) {
            if (newEvent.getStateAction().contains("REJECT") || newEvent.getStateAction().contains("CANCEL")) {
                state = EventState.CANCELED;
            } else if (newEvent.getStateAction().contains("SEND")) {
                state = EventState.PENDING;
            } else {
                state = EventState.PUBLISHED;
            }
        } else {
            state = null;
        }
        return state;
    }
}
