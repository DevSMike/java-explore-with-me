package ru.practicum.ewm.main.subscription;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.main.dto.event.EventFullDto;
import ru.practicum.ewm.main.dto.subscription.PrivateSearchSubsParams;
import ru.practicum.ewm.main.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.Subscription;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.model.event.Location;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.SubscriptionRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.ewm.main.service.subscription.SubscriptionServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.practicum.ewm.main.mapper.SubscriptionMapper.toSubscriptionDto;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    private static final PrivateSearchSubsParams PARAMS = PrivateSearchSubsParams.builder()
            .subId(1L)
            .initId(2L)
            .from(0)
            .size(10)
            .categories(List.of(1L, 2L))
            .rangeEnd(null)
            .rangeStart(null)
            .text("GYM")
            .sort(null)
            .build();

    private User subscriber;
    private User init1;
    private Subscription sub1OnInit1;
    private Subscription subOnInit2;
    private Event eventInit1;
    private Event eventInit2;

    @Mock
    SubscriptionRepository subscriptionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    public void fillData() {
        subscriber = User.builder()
                .email("sub@mail.ru")
                .name("subscriber")
                .id(1L)
                .build();

        init1 = User.builder()
                .email("init_1@mail.ru")
                .name("init_1")
                .id(2L)
                .build();

        User init2 = User.builder()
                .email("init_2@mail.ru")
                .name("init_2")
                .id(3L)
                .build();

        sub1OnInit1 = Subscription.builder()
                .isSubscribed(true)
                .timestamp(LocalDateTime.now())
                .initiator(init1)
                .subscriber(subscriber)
                .build();

        subOnInit2 = Subscription.builder()
                .isSubscribed(true)
                .timestamp(LocalDateTime.now())
                .initiator(init2)
                .subscriber(subscriber)
                .build();

        Category cat1 = Category.builder()
                .id(1L)
                .name("Running")
                .build();

        Category cat2 = Category.builder()
                .id(2L)
                .name("Swimming")
                .build();

        eventInit1 = Event.builder()
                .initiator(init1)
                .annotation("Running to GYM SPORT")
                .description("Running in SPB with great people!")
                .category(cat1)
                .eventDate(LocalDateTime.of(LocalDate.of(2020, 5, 13), LocalTime.of(6, 30)))
                .state(EventState.PUBLISHED)
                .isPaid(false)
                .isRequestModeration(false)
                .participantLimit(100)
                .location(Location.builder()
                        .lat(180.1f)
                        .lon(93.2121f)
                        .build())
                .createdOn(LocalDateTime.now())
                .title("Running")
                .build();

        eventInit2 = Event.builder()
                .initiator(init2)
                .annotation("Swimming  GYM SPORT")
                .description("Swimming in SPB with great people!")
                .category(cat2)
                .eventDate(LocalDateTime.of(LocalDate.of(2023, 10, 13), LocalTime.of(6, 30)))
                .state(EventState.PUBLISHED)
                .isPaid(false)
                .isRequestModeration(false)
                .participantLimit(100)
                .location(Location.builder()
                        .lat(190.1f)
                        .lon(123.2121f)
                        .build())
                .createdOn(LocalDateTime.now())
                .title("Swimming")
                .build();
    }

    @Test
    void subscribeOnInitOneBySub_whenAllDataIsCorrect_thenReturnNewSubscription() {
        when(subscriptionRepository.findSubscriptionByIds(sub1OnInit1.getInitiator().getId(), sub1OnInit1.getSubscriber().getId(),
                sub1OnInit1.isSubscribed())).thenReturn(Optional.empty());
        when(userRepository.findById(init1.getId())).thenReturn(Optional.of(init1));
        when(userRepository.findById(subscriber.getId())).thenReturn(Optional.of(subscriber));

        SubscriptionDto sub = subscriptionService.subscribe(toSubscriptionDto(sub1OnInit1));
        assertEquals(toSubscriptionDto(sub1OnInit1).getInitId(), sub.getInitId());
        assertEquals(toSubscriptionDto(sub1OnInit1).getIsSub(), true);

    }

    @Test
    void subscribeOnInitOneBySub_whenSubscriptionIsExisted_thenThrowNewIncorrectDataException() {
        when(subscriptionRepository.findSubscriptionByIds(sub1OnInit1.getInitiator().getId(), sub1OnInit1.getSubscriber().getId(),
                sub1OnInit1.isSubscribed())).thenReturn(Optional.of(sub1OnInit1));

        IncorrectDataException exp = assertThrows(IncorrectDataException.class,
                () -> subscriptionService.subscribe(toSubscriptionDto(sub1OnInit1)));

        assertEquals("Error: subscription. Нельзя второй раз подписываться на инициатора", exp.getMessage());
    }

    @Test
    void subscribeOnInitOneBySub_whenInitiatorDoesNotExist_thenThrowNoDataException() {
        when(subscriptionRepository.findSubscriptionByIds(sub1OnInit1.getInitiator().getId(), sub1OnInit1.getSubscriber().getId(),
                sub1OnInit1.isSubscribed())).thenReturn(Optional.empty());
        when(userRepository.findById(init1.getId())).thenReturn(Optional.empty());

        NoDataException exp = assertThrows(NoDataException.class,
                () -> subscriptionService.subscribe(toSubscriptionDto(sub1OnInit1)));

        assertEquals("Error: sub. Инициатора не существует", exp.getMessage());
    }

    @Test
    void subscribeOnInitOneBySub_whenInitiatorAndSubscriberIsOnePerson_thenThrowNewIncorrectDataException() {
        when(subscriptionRepository.findSubscriptionByIds(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(Optional.empty());
        sub1OnInit1.setSubscriber(init1);

        IncorrectDataException exp = assertThrows(IncorrectDataException.class,
                () -> subscriptionService.subscribe(toSubscriptionDto(sub1OnInit1)));

        assertEquals("Error: subscription. Нельзя подписаться/отписаться на/от самого себя", exp.getMessage());
    }

    @Test
    void subscribeOnInitOneBySub_whenSubscriptionSubIsFalse_thenThrowNewIncorrectDataException() {
        when(subscriptionRepository.findSubscriptionByIds(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(Optional.empty());
        sub1OnInit1.setSubscribed(false);

        IncorrectDataException exp = assertThrows(IncorrectDataException.class,
                () -> subscriptionService.subscribe(toSubscriptionDto(sub1OnInit1)));

        sub1OnInit1.setSubscribed(true);
        assertEquals("Error: subscription. Переданы некорректные данные в dto. Value: false", exp.getMessage());
    }

    @Test
    void unSubscribeOnInitOneBySub_whenSubscriptionSubIsTrue_thenThrowNewIncorrectDataException() {
        when(subscriptionRepository.findSubscriptionByIds(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(Optional.of(sub1OnInit1));
        sub1OnInit1.setSubscribed(true);

        IncorrectDataException exp = assertThrows(IncorrectDataException.class,
                () -> subscriptionService.unSubscribe(toSubscriptionDto(sub1OnInit1)));

        assertEquals("Error: subscription. Переданы некорректные данные в dto. Value: true", exp.getMessage());
    }

    @Test
    void unSubscribeOnInitOneBySub_whenSubscriptionDoesNotExist_thenThrowNewIncorrectDataException() {
        when(subscriptionRepository.findSubscriptionByIds(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(Optional.empty());

        IncorrectDataException exp = assertThrows(IncorrectDataException.class,
                () -> subscriptionService.unSubscribe(toSubscriptionDto(sub1OnInit1)));

        assertEquals("Error: subscription. Нельзя отписаться, на кого не подписан", exp.getMessage());
    }

    @Test
    void unSubscribeOnInitOneBySub_whenAllDataIsCorrect_thenReturnNewSubscription() {
        when(subscriptionRepository.findSubscriptionByIds(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(Optional.of(sub1OnInit1));
        when(userRepository.findById(init1.getId())).thenReturn(Optional.of(init1));
        when(userRepository.findById(subscriber.getId())).thenReturn(Optional.of(subscriber));
        sub1OnInit1.setSubscribed(false);

        SubscriptionDto sub = subscriptionService.unSubscribe(toSubscriptionDto(sub1OnInit1));

        assertEquals(toSubscriptionDto(sub1OnInit1).getInitId(), sub.getInitId());
        assertEquals(toSubscriptionDto(sub1OnInit1).getIsSub(), false);
        sub1OnInit1.setSubscribed(true);
    }

    @Test
    void getEventsByInitiatorOneId_whenAllDataIsCorrect_thenReturnListOfOneEvent() {

        when(subscriptionRepository.findSubscriptionByIds(sub1OnInit1.getInitiator().getId(),
                sub1OnInit1.getSubscriber().getId(), sub1OnInit1.isSubscribed()))
                .thenReturn(Optional.of(sub1OnInit1));
        when(eventRepository.findEventsByPublicSearchSubscriptions(anyList(), anyString(), anyList(), any(),
                any(), any(EventState.class), any(PageRequest.class))).thenReturn(List.of(eventInit1));

        List<EventFullDto> events = subscriptionService.getEventsByInitiatorId(PARAMS);

        assertEquals(events.size(), 1);
    }

    @Test
    void getEventsByInitiatorOneId_whenSubscriptionOnInitDoesNotExist_thenThrowIncorrectDataException() {
        when(subscriptionRepository.findSubscriptionByIds(sub1OnInit1.getInitiator().getId(), sub1OnInit1.getSubscriber().getId(),
                sub1OnInit1.isSubscribed())).thenReturn(Optional.empty());

        IncorrectDataException exp = assertThrows(IncorrectDataException.class,
                () -> subscriptionService.getEventsByInitiatorId(PARAMS));

        assertEquals("Error: subscription. Пользователь не подписан на инициатора", exp.getMessage());
    }

    @Test
    void getAllSubscribedEvents_whenAllDataIsCorrectCheckSortByDefault_thenReturnListOfTwoEvent() {
        when(subscriptionRepository.findSubscriptionsBySubscriberId(subscriber.getId()))
                .thenReturn(Optional.of(List.of(sub1OnInit1, subOnInit2)));
        when(eventRepository.findEventsByPublicSearchSubscriptions(anyList(), anyString(), anyList(), any(),
                any(), any(EventState.class), any(PageRequest.class))).thenReturn(List.of(eventInit2, eventInit1));

        List<EventFullDto> events = subscriptionService.getAllSubscribedEvents(PARAMS);

        assertEquals(events.size(), 2);
        assertEquals(events.get(0).getInitiator().getId(), init1.getId());
    }

    @Test
    void getAllSubscribedEvents_whenSubscriptionOnInitDoesNotExist_thenThrowNoDataException() {
        when(subscriptionRepository.findSubscriptionsBySubscriberId(subscriber.getId()))
                .thenReturn(Optional.empty());

        NoDataException exp = assertThrows(NoDataException.class,
                () -> subscriptionService.getAllSubscribedEvents(PARAMS));

        assertEquals("Error: subscription. There is no data when search subscriptions by id", exp.getMessage());
    }
}