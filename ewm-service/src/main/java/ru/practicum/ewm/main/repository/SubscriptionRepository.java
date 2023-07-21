package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.model.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s where s.subscriber.id = :subId AND initiator.id = :initId " +
            "AND s.isSubscribed = :isSub")
    Optional<Subscription> findSubscriptionByIds(long initId, long subId, boolean isSub);

    @Query("select s from Subscription s where s.subscriber.id = :subId AND s.isSubscribed = true")
    Optional<List<Subscription>> findSubscriptionsBySubscriberId(long subId);
}
