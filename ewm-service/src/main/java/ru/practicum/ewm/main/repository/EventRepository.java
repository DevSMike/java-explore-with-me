package ru.practicum.ewm.main.repository;

import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    default List<Event> findEventsBySearchWithSpec(List<Long> userIds, List<Long> categories, List<EventState> states,
                                                   LocalDateTime start, LocalDateTime end, Pageable page) {
        Specification<Event> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userIds != null) {
                predicates.add(root.get("initiator").get("id").in(userIds));
            }
            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (states != null) {
                predicates.add(root.get("state").in(states));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return findAll(specification, page).getContent();
    }

    default List<Event> findEventsByPublicSearchOnlyAvailableWithSpec(String text, List<Long> categories, Boolean paid,
                                                                      LocalDateTime start, LocalDateTime end,
                                                                      EventState state, Pageable page) {
        Specification<Event> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPaid"), paid));
            }
            if (text != null) {
                String likeText = "%" + text.toLowerCase() + "%";
                Predicate annotationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), likeText);
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeText);
                predicates.add(criteriaBuilder.or(annotationPredicate, descriptionPredicate));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }
            if (state != null) {
                predicates.add(criteriaBuilder.equal(root.get("state"), state));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return findAll(specification, page).getContent();
    }

    default List<Event> findEventsByPublicSearchWithSpec(String text, List<Long> categories, Boolean paid, LocalDateTime start, LocalDateTime end, EventState state, Pageable page) {
        Specification<Event> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPaid"), paid));
            }
            if (text != null) {
                String likeText = "%" + text.toLowerCase() + "%";
                Predicate annotationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), likeText);
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeText);
                predicates.add(criteriaBuilder.or(annotationPredicate, descriptionPredicate));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }
            if (state != null) {
                predicates.add(criteriaBuilder.equal(root.get("state"), state));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return findAll(specification, page).getContent();
    }

    @Query("select e from Event e where e.initiator.id = :userId")
    List<Event> getUserEvents(long userId, Pageable page);

    @Query("select e from Event e where e.initiator.id = :userId AND e.id = :eventId")
    Optional<Event> findEventById(long userId, long eventId);

    @Query("select e from Event e where e.id IN :ids")
    List<Event> findEventsByIds(List<Long> ids);

    @Query("select e from Event e where e.category.id = :id")
    List<Event> findEventByCategory(long id);
}
