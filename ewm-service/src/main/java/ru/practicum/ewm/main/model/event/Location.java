package ru.practicum.ewm.main.model.event;

import lombok.*;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class Location {

    private float lat;
    private float lon;
}
