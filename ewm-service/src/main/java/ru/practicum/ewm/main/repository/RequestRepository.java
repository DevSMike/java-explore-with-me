package ru.practicum.ewm.main.repository;

import ru.practicum.ewm.main.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r from Request r where r.event.id = :eventId AND r.requester.id = :requesterId")
    Optional<Request> findUserRequestToEvent(long eventId, long requesterId);

    @Query("select r from Request r where r.requester.id = :userId")
    List<Request> findUserRequests(long userId);

    @Query("select r from Request r where r.event.id = :eventId")
    List<Request> findRequestsToEvent(long eventId);

    @Query("select r from Request r where r.id IN :ids")
    List<Request> findRequestsByIds(List<Long> ids);
}
