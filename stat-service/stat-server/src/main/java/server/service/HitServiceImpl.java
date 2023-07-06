package server.service;

import dto.HitDto;
import dto.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.repository.HitRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static mapper.EndpointHitMapper.*;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Override
    public HitDto createHit(HitDto hitDto) {
        hitRepository.save(toEndpointHit(hitDto));
        return hitDto;
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        List<StatsDto> stats = new ArrayList<>();

        if (uris == null) {
            uris = new ArrayList<>(hitRepository.findAllHipsBetweenDates(toDateFromString(start), toDateFromString(end)));
        }

        for (String uri : uris) {
            long hits;
            if (unique) {
                hits = hitRepository.findHitByUriAndUniqueIp(toDateFromString(start), toDateFromString(end), uri);
            } else {
                hits = hitRepository.findHitByUriNotUnique(toDateFromString(start), toDateFromString(end), uri);
            }
            stats.add(toStatsDto("ewm-main-service", uri, hits));
        }
        return stats.stream()
                .sorted(Comparator.comparingLong(x->-x.getHits()))
                .collect(Collectors.toList());
    }
}
