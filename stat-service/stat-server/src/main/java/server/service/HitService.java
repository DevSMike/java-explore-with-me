package server.service;

import dto.HitDto;
import dto.StatsDto;

import java.util.List;

public interface HitService {

    HitDto createHit(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

}
