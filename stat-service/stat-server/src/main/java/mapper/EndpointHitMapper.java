package mapper;

import dto.HitDto;
import dto.StatsDto;
import server.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static EndpointHit toEndpointHit(HitDto hitDto) {
        return EndpointHit.builder()
                .app(hitDto.getApp())
                .ip(hitDto.getIp())
                .timestamp(toDateFromString(hitDto.getTimestamp()))
                .uri(hitDto.getUri())
                .build();
    }

    public static LocalDateTime toDateFromString(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public static StatsDto toStatsDto(String app, String uri, long hits) {
        return StatsDto.builder()
                .app(app)
                .uri(uri)
                .hits(hits)
                .build();
    }
}
