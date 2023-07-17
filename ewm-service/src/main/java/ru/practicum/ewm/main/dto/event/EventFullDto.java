package ru.practicum.ewm.main.dto.event;

import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.model.event.Location;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class EventFullDto {

    private long id;
    private String annotation;
    private String title;
    private int confirmedRequests;
    private String createdOn;
    private String eventDate;
    private String publishedOn;
    private String description;
    private UserDto initiator;
    private CategoryDto category;
    private Location location;
    private Boolean paid;
    private Boolean requestModeration;
    private int participantLimit;
    private EventState state;
    private int views;
}
