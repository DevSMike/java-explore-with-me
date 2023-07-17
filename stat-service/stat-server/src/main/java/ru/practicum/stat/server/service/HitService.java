package ru.practicum.stat.server.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.util.List;

public interface HitService {

    HitDto createHit(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

}
