package ru.practicum.ewm.main.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.main.model.Subscription;
import ru.practicum.ewm.main.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {

    public static Subscription toSubscription(SubscriptionDto subscriptionDto, List<User> users) {
        return Subscription.builder()
                .isSubscribed(subscriptionDto.getIsSub())
                .timestamp(subscriptionDto.getTimestamp())
                .initiator(users.get(0))
                .subscriber(users.get(1))
                .build();
    }

    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .subId(subscription.getSubscriber().getId())
                .initId(subscription.getInitiator().getId())
                .isSub(subscription.isSubscribed())
                .timestamp(subscription.getTimestamp())
                .build();
    }

    public static Subscription toSubscriptionUpdate(SubscriptionDto subscriptionDto, Long subId, Subscription subFromDb) {
        return Subscription.builder()
                .id(subId)
                .isSubscribed(subscriptionDto.getIsSub())
                .timestamp(subscriptionDto.getTimestamp())
                .subscriber(subFromDb.getSubscriber())
                .initiator(subFromDb.getInitiator())
                .build();
    }

}
