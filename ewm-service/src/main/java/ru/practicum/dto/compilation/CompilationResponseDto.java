package ru.practicum.dto.compilation;

import ru.practicum.dto.event.EventShortDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CompilationResponseDto {

    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
