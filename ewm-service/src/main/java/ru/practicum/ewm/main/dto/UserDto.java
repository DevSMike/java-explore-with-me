package ru.practicum.ewm.main.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserDto {

    private long id;
    @NotBlank
    private String name;
    @Email @NotBlank
    private String email;
}
