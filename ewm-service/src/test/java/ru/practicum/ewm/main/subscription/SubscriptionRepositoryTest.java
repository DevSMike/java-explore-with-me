package ru.practicum.ewm.main.subscription;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.client.StatsClient;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.Subscription;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.model.event.Location;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.SubscriptionRepository;
import ru.practicum.ewm.main.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@DataJpaTest
@ActiveProfiles("test")
public class SubscriptionRepositoryTest {

    private Long init1Id;
    private Long init2Id;
    private Long cat1Id;
    private Long cat2Id;
    private Long subId;


    private static final Pageable PAGE = PageRequest.of(0, 10);

    @MockBean
    StatsClient statsClient;

    @MockBean
    HttpServletRequest httpServletRequest;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @BeforeEach
    public void fillData() {
        User initiator = User.builder()
                .email("initiator1@mail.ru")
                .name("Initiator_1")
                .build();

        User initiator2 = User.builder()
                .email("initiator2@mail.ru")
                .name("Initiator_2")
                .build();

        User subscriber = User.builder()
                .email("sub1@mail.ru")
                .name("sub_1")
                .build();

        Category cat1 = Category.builder()
                .name("Running")
                .build();

        Category cat2 = Category.builder()
                .name("Swimming")
                .build();

        initiator = userRepository.save(initiator);
        init1Id = initiator.getId();

        initiator2 = userRepository.save(initiator2);
        init2Id = initiator2.getId();

        subscriber = userRepository.save(subscriber);
        subId = subscriber.getId();

        cat1 = categoryRepository.save(cat1);
        cat1Id = cat1.getId();

        cat2 = categoryRepository.save(cat2);
        cat2Id = cat2.getId();

        Event eventByInitiator1 = Event.builder()
                .initiator(initiator)
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

        Event eventByInitiator2 = Event.builder()
                .initiator(initiator2)
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

        eventRepository.save(eventByInitiator1);
        eventRepository.save(eventByInitiator2);
    }

    @Test
    void findAllSubscribedEventsOnTwoInitiators_whenCatsAreNullAndEndIsNull_thenReturnListOfOneEvent() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id, init2Id),
                "Swim", null, LocalDateTime.now(), null, EventState.PUBLISHED, PAGE);

        assertEquals(1, result.size());
        assertEquals("Swimming  GYM SPORT", result.get(0).getAnnotation());
    }

    @Test
    void findAllSubscribedEventsOnTwoInitiators_whenAllAgrsAreNull_thenReturnListOfTwoEvents() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id, init2Id),
                null, null, null, null, EventState.PUBLISHED, PAGE);

        assertEquals(2, result.size());
    }

    @Test
    void findAllSubscribedEventsOnTwoInitiators_whenAllDataIsCorrect_thenReturnListOfTwoEvents() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id, init2Id),
                "GYM", List.of(cat1Id, cat2Id), LocalDateTime
                        .of(LocalDate.of(2010, 12, 12), LocalTime.of(6, 30)),
                LocalDateTime
                        .of(LocalDate.of(2040, 12, 12), LocalTime.of(6, 30)),
                EventState.PUBLISHED, PAGE);

        assertEquals(2, result.size());
    }

    @Test
    void findAllSubscribedEventsOnFirstInitiator_whenTextIsFromSecondInitEvent_thenReturnListOfZeroEvents() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id),
                "Swim", null, null, null, EventState.PUBLISHED, PAGE);

        assertEquals(0, result.size());
    }

    @Test
    void findAllSubscribedEventsOnFirstInitiator_whenTextIsCorrectAndCategoryIsFromSecondInitEvent_thenReturnListOfZeroEvents() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id),
                "Run", List.of(cat2Id), null, null, EventState.PUBLISHED, PAGE);

        assertEquals(0, result.size());
    }

    @Test
    void findAllSubscribedEventsOnFirstInitiator_whenEventDateIsBeforeStart_thenReturnListOfZeroEvents() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id),
                null, null, LocalDateTime.now(), null, EventState.PUBLISHED, PAGE);

        assertEquals(0, result.size());
    }

    @Test
    void findAllSubscribedEventsOnSecondInitiator_whenAllDataCorrectAndCategoryIsFromFirstInitEvent_thenReturnListOfZeroEvents() {

        List<Event> result = eventRepository.findEventsByPublicSearchSubscriptions(List.of(init1Id),
                "Swim", List.of(cat1Id), LocalDateTime.now(), null, EventState.PUBLISHED, PAGE);

        assertEquals(0, result.size());
    }

    @Test
    void findSubscriptionsBySubId_whenSubbedOnTwoUsers_thenReturnListOfTwoSubscriptions() {
        subscriptionRepository.save(Subscription.builder()
                .initiator(userRepository.findById(init1Id).orElse(null))
                .subscriber(userRepository.findById(subId).orElse(null))
                .timestamp(LocalDateTime.now())
                .isSubscribed(true)
                .build());

        subscriptionRepository.save(Subscription.builder()
                .initiator(userRepository.findById(init2Id).orElse(null))
                .subscriber(userRepository.findById(subId).orElse(null))
                .timestamp(LocalDateTime.now())
                .isSubscribed(true)
                .build());

        List<Subscription> result = subscriptionRepository.findSubscriptionsBySubscriberId(subId).orElse(new ArrayList<>());

        assertEquals(2, result.size());
    }

    @Test
    void findSubscriptionsBySubIdAndInitId_whenSubbedOnTwoUsers_thenReturnListOfOneSubscriptions() {
        subscriptionRepository.save(Subscription.builder()
                .initiator(userRepository.findById(init1Id).orElse(null))
                .subscriber(userRepository.findById(subId).orElse(null))
                .timestamp(LocalDateTime.now())
                .isSubscribed(true)
                .build());

        subscriptionRepository.save(Subscription.builder()
                .initiator(userRepository.findById(init2Id).orElse(null))
                .subscriber(userRepository.findById(subId).orElse(null))
                .timestamp(LocalDateTime.now())
                .isSubscribed(true)
                .build());

        Subscription sub = subscriptionRepository.findSubscriptionByIds(init1Id, subId, true)
                .orElse(Subscription.builder().build());

        assertEquals(sub.getInitiator().getId(), init1Id);
    }

    @Test
    void findSubscriptionsBySubId_whenSubbedOnTwoUsersAndUnsubbedFromInit1_thenReturnListOfOneSubscriptions() {
        Subscription subscription = subscriptionRepository.save(Subscription.builder()
                .initiator(userRepository.findById(init1Id).orElse(null))
                .subscriber(userRepository.findById(subId).orElse(null))
                .timestamp(LocalDateTime.now())
                .isSubscribed(true)
                .build());

        subscriptionRepository.save(Subscription.builder()
                .initiator(userRepository.findById(init2Id).orElse(null))
                .subscriber(userRepository.findById(subId).orElse(null))
                .timestamp(LocalDateTime.now())
                .isSubscribed(true)
                .build());

        subscription.setSubscribed(false);
        subscriptionRepository.save(subscription);

        List<Subscription> result = subscriptionRepository.findSubscriptionsBySubscriberId(subId).orElse(new ArrayList<>());
        assertEquals(1, result.size());
    }

    @AfterEach
    public void deleteSubs() {
        subscriptionRepository.deleteAll();
    }

}
