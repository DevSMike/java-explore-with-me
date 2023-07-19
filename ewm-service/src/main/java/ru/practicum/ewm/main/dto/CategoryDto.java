package ru.practicum.ewm.main.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CategoryDto {

    private long id;
    @NotBlank
    private String name;
}
