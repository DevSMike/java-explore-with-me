package ru.practicum.ewm.main.service.subscription;

import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.subscription.PrivateSearchSubsParams;
import ru.practicum.ewm.main.dto.subscription.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto subscribe(SubscriptionDto subscription);

    SubscriptionDto unSubscribe(SubscriptionDto subscription);

    List<EventFullDto> getEventsByInitiatorId(PrivateSearchSubsParams params);

    List<EventFullDto> getAllSubscribedEvents(PrivateSearchSubsParams params);
}
