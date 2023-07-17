package ru.practicum.ewm.main.service.event;

import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventDto eventFullDto, long userId);

    List<EventFullDto> getUserEvents(long userId, int from, int size);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto updateEventById(long userId, long eventId, NewEventDto event);

    List<EventFullDto> findEventsBySearch(List<Long> userIds, List<Long> categoriesIds,
                                          List<String> states, String rangeStart, String rangeEnd,
                                          int from, int size);

    EventFullDto updateEventByAdmin(long eventId, NewEventDto event);

    List<EventShortDto> findEventsByPublicSearch(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                                 String ip);

    EventFullDto getUserEventByIdPublic(long eventId, String ip);

}
