package http.client.hit;

import dto.HitDto;
import http.client.exception.IncorrectDataException;
import http.client.exception.IpIsNullException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class StatsController {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Logger log = LoggerFactory.getLogger(StatsController.class);
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> createHit(@RequestBody HitDto hitDto) {
        if (hitDto.getIp() == null || hitDto.getIp().isEmpty()) {
            throw new IpIsNullException();
        }
        log.debug("Gateway: creating hit");
        return statsClient.createHit(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start, @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris, @RequestParam(required = false) Boolean unique) {


        LocalDateTime startDate = LocalDateTime.parse(start, DTF);
        LocalDateTime endDate = LocalDateTime.parse(end, DTF);

        if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
            throw new IncorrectDataException();
        }
        log.debug("Gateway: getting stats");
        return statsClient.getStats(start, end, uris, unique);
    }
}
