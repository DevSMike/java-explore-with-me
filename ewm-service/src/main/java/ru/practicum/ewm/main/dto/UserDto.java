package ru.practicum.ewm.main.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserDto {

    private long id;
    private String name;
    private String email;
}
