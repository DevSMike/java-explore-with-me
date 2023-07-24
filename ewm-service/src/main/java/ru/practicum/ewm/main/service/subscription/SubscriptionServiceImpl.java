package ru.practicum.ewm.main.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.subscription.PrivateSearchSubsParams;
import ru.practicum.ewm.main.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.EventMapper;
import ru.practicum.ewm.main.model.Subscription;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.SubscriptionRepository;
import ru.practicum.ewm.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.mapper.EventMapper.toDateFromString;
import static ru.practicum.ewm.main.mapper.EventMapper.toStringFromDate;
import static ru.practicum.ewm.main.mapper.SubscriptionMapper.*;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public SubscriptionDto subscribe(SubscriptionDto subscription) {
        if (subscriptionRepository.findSubscriptionByIds(subscription.getInitId(), subscription.getSubId(),
                subscription.getIsSub()).isPresent()) {
            throw new IncorrectDataException("Error: subscription. Нельзя второй раз подписываться на инициатора");
        }
        List<User> usersFromDb = checkSubscriptionDataAndReturnUsers(subscription, true);
        subscription.setTimestamp(toDateFromString(toStringFromDate(LocalDateTime.now())));
        Subscription subscriptionToSave = toSubscription(subscription, usersFromDb);
        subscriptionRepository.save(subscriptionToSave);
        return toSubscriptionDto(subscriptionToSave);
    }

    @Override
    public SubscriptionDto unSubscribe(SubscriptionDto subscription) {
        Subscription subFromDb = subscriptionRepository.findSubscriptionByIds(subscription.getInitId(),
                subscription.getSubId(), true).orElse(null);

        if (subFromDb == null) {
            throw new IncorrectDataException("Error: subscription. Нельзя отписаться, на кого не подписан");
        }
        checkSubscriptionDataAndReturnUsers(subscription, false);
        subscription.setTimestamp(toDateFromString(toStringFromDate(LocalDateTime.now())));
        Subscription updated = toSubscriptionUpdate(subscription, subFromDb.getId(), subFromDb);
        subscriptionRepository.save(updated);
        return toSubscriptionDto(updated);
    }

    @Override
    public List<EventFullDto> getEventsByInitiatorId(PrivateSearchSubsParams params) {
        if (subscriptionRepository.findSubscriptionByIds(params.getInitId(), params.getSubId(), true).isEmpty()) {
            throw new IncorrectDataException("Error: subscription. Пользователь не подписан на инициатора");
        }

        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());
        List<Optional<LocalDateTime>> dates = checkDatesInSearchAndReturns(params);
        params.setSort(setDefaultValueToSort(params));

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(Collections
                        .singletonList(params.getInitId()), params.getText(), params.getCategories(),
                dates.get(0).orElse(null), dates.get(1).orElse(null), EventState.PUBLISHED, page);

        result = getEventsBySort(params, result);

        return result.stream()
                .map(EventMapper::toEventFullDtoFromEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getAllSubscribedEvents(PrivateSearchSubsParams params) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsBySubscriberId(params.getSubId())
                .orElseThrow(() -> new NoDataException("Error: subscription. There is no data when search subscriptions by id"));

        Pageable page = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        List<Long> initiatorsId = subscriptions.stream()
                .map(x -> x.getInitiator().getId())
                .collect(Collectors.toList());

        List<Optional<LocalDateTime>> dates = checkDatesInSearchAndReturns(params);

        params.setSort(setDefaultValueToSort(params));

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(initiatorsId, params.getText(),
                params.getCategories(), dates.get(0).orElse(null), dates.get(1).orElse(null),
                EventState.PUBLISHED, page);

        result = getEventsBySort(params, result);

        return result.stream()
                .map(EventMapper::toEventFullDtoFromEvent)
                .collect(Collectors.toList());
    }

    private List<User> checkSubscriptionDataAndReturnUsers(SubscriptionDto subscription, Boolean isSub) {
        if (subscription.getSubId().equals(subscription.getInitId())) {
            throw new IncorrectDataException("Error: subscription. Нельзя подписаться/отписаться на/от самого себя");
        }

        if (!subscription.getIsSub() && isSub) {
            throw new IncorrectDataException("Error: subscription. Переданы некорректные данные в dto. Value: false");
        }

        if (subscription.getIsSub() && !isSub) {
            throw new IncorrectDataException("Error: subscription. Переданы некорректные данные в dto. Value: true");
        }

        List<User> users = new ArrayList<>();
        User initFromDb = userRepository.findById(subscription.getInitId())
                .orElseThrow(() -> new NoDataException("Error: sub. Инициатора не существует"));
        User subFromDb = userRepository.findById(subscription.getSubId())
                .orElseThrow(() -> new NoDataException("Error: sub. Подписчика не существует"));

        users.add(initFromDb);
        users.add(subFromDb);
        return users;
    }


    private String setDefaultValueToSort(PrivateSearchSubsParams params) {

        if (params.getSort() == null) {
            params.setSort("EVENT_DATE");
        }
        return params.getSort();
    }

    private List<Optional<LocalDateTime>> checkDatesInSearchAndReturns(PrivateSearchSubsParams params) {

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = null;

        if (params.getRangeStart() != null) {
            startDate = toDateFromString(params.getRangeStart());
        }

        if (params.getRangeEnd() != null) {
            endDate = toDateFromString(params.getRangeEnd());
        }

        return List.of(Optional.of(startDate), Optional.ofNullable(endDate));
    }

    private List<Event> getEventsBySort(PrivateSearchSubsParams params, List<Event> result) {
        switch (params.getSort()) {
            case "EVENT_DATE": {
                return result.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .collect(Collectors.toList());

            }
            case "VIEWS": {
                return result.stream()
                        .sorted(Comparator.comparingLong(x -> -x.getViews()))
                        .collect(Collectors.toList());

            }
            default: {
                throw new IncorrectDataException("Error: subscription. Такой сортировки не существует");
            }
        }
    }

}
