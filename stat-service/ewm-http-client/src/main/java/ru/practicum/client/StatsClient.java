package ru.practicum.client;

import ru.practicum.client.base.BaseClient;
import ru.practicum.dto.HitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stat.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createHit(HitDto dto) {
        return post(dto);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        StringBuilder url = new StringBuilder("?");

        if (unique == null) {
            unique = false;
        }
        if (uris != null) {
            for (String uri : uris) {
                url.append("&uris=").append(uri);
            }
            url.append("&");
        }

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique
        );

        return get("/stats" + url + "start={start}&end={end}&unique={unique}", parameters);
    }
}
