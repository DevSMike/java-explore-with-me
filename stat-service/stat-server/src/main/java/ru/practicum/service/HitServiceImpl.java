package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.HitRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Override
    public HitDto createHit(HitDto hitDto) {
        hitRepository.save(EndpointHitMapper.toEndpointHit(hitDto));
        return hitDto;
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        List<StatsDto> stats = new ArrayList<>();

        if (uris == null) {
            uris = new ArrayList<>(hitRepository.findAllHipsBetweenDates(EndpointHitMapper.toDateFromString(start), EndpointHitMapper.toDateFromString(end)));
        }

        if (unique == null) {
            unique = false;
        }

        for (String uri : uris) {
            long hits;
            if (unique) {
                hits = hitRepository.findHitByUriAndUniqueIp(EndpointHitMapper.toDateFromString(start), EndpointHitMapper.toDateFromString(end), uri);
            } else {
                hits = hitRepository.findHitByUriNotUnique(EndpointHitMapper.toDateFromString(start), EndpointHitMapper.toDateFromString(end), uri);
            }
            stats.add(EndpointHitMapper.toStatsDto("ewm-main-service", uri, hits));
        }
        return stats.stream()
                .sorted(Comparator.comparingLong(x -> -x.getHits()))
                .collect(Collectors.toList());
    }
}
