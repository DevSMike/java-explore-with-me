package ru.practicum.ewm.main.model.event;

import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "events")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 2000)
    private String annotation;
    @Column(length = 120)
    private String title;
    private int confirmedRequests;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @Column(length = 7000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Embedded
    private Location location;
    private Boolean isPaid;
    private Boolean isRequestModeration;
    private int participantLimit;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private Integer views;
}
