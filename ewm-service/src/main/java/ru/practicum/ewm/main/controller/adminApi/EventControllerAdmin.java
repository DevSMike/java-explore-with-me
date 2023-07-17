package ru.practicum.ewm.main.controller.adminApi;

import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.event.NewEventDto;
import ru.practicum.ewm.main.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin")
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping("/events")
    public List<EventFullDto> getEventsBySearch(@RequestParam(required = false) List<Long> users, @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories, @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd, @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return eventService.findEventsBySearch(users, categories, states, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable @Min(0) long eventId, @RequestBody NewEventDto event) {
        log.debug("Admin: updating event with id: {}", eventId);
        return eventService.updateEventByAdmin(eventId, event);
    }
}
