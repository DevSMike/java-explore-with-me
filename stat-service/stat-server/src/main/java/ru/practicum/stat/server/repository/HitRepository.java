package ru.practicum.stat.server.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.stat.server.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT COUNT(e) FROM EndpointHit e WHERE e.uri = :uri AND e.timestamp BETWEEN :start AND :end")
    long findHitByUriNotUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query("SELECT COUNT(DISTINCT e.ip) FROM EndpointHit e WHERE e.uri = :uri AND e.timestamp BETWEEN :start AND :end")
    long findHitByUriAndUniqueIp(LocalDateTime start, LocalDateTime end, String uri);

    @Query("SELECT e.uri FROM EndpointHit e WHERE e.timestamp BETWEEN :start AND :end")
    List<String> findAllHipsBetweenDates(LocalDateTime start, LocalDateTime end);
}
