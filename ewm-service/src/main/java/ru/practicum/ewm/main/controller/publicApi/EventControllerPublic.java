package ru.practicum.ewm.main.controller.publicApi;

import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.dto.search.PublicSearchEventsParamsDto;
import ru.practicum.ewm.main.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/events")
public class EventControllerPublic {

    private final EventService eventService;
    private final HttpServletRequest request;

    @GetMapping()
    public List<EventShortDto> getEventsBySearch(@RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid, @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd, @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam(required = false) String sort, @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.debug("Public: search events");
        PublicSearchEventsParamsDto params = PublicSearchEventsParamsDto.builder()
                .ip(request.getRemoteAddr())
                .categories(categories)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .text(text)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .from(from)
                .size(size)
                .paid(paid)
                .build();
        return eventService.findEventsByPublicSearch(params);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable long eventId) {
        log.debug("Public: getting event by id: {}", eventId);
        return eventService.getUserEventByIdPublic(eventId, request.getRemoteAddr());
    }
}
