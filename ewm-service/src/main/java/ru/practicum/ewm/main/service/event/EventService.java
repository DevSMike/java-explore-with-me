package ru.practicum.ewm.main.service.event;

import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.dto.search.AdminSearchEventsParamsDto;
import ru.practicum.ewm.main.dto.search.PublicSearchEventsParamsDto;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventDto eventFullDto, long userId);

    List<EventFullDto> getUserEvents(long userId, int from, int size);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto updateEventById(long userId, long eventId, NewEventDto event);

    List<EventFullDto> findEventsBySearch(AdminSearchEventsParamsDto params);

    EventFullDto updateEventByAdmin(long eventId, NewEventDto event);

    List<EventShortDto> findEventsByPublicSearch(PublicSearchEventsParamsDto params);

    EventFullDto getUserEventByIdPublic(long eventId, String ip);

}
