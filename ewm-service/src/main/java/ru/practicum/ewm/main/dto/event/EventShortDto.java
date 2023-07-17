package ru.practicum.ewm.main.dto.event;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.UserDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class EventShortDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
