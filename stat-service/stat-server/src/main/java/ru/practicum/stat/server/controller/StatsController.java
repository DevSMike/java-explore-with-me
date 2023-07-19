package ru.practicum.stat.server.controller;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.server.service.HitService;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto createHit(@RequestBody HitDto hitDto) {
        return hitService.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam String start, @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris, @RequestParam(required = false) Boolean unique) {

        return hitService.getStats(start, end, uris, unique);
    }

}
