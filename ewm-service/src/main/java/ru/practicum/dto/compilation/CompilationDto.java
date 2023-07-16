package ru.practicum.dto.compilation;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CompilationDto {


    private List<Long> events;
    private Boolean pinned;
    private String title;
}
