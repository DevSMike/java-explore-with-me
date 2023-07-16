package ru.practicum.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CategoryDto {

    private long id;
    private String name;
}
