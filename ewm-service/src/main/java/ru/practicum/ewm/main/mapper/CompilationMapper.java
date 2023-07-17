package ru.practicum.ewm.main.mapper;

import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.main.dto.event.EventShortDto;
import ru.practicum.ewm.main.model.Compilation;

import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(CompilationDto compilation) {
        return Compilation.builder()
                .events(compilation.getEvents())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }

    public static CompilationResponseDto toCompilationResponseDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationResponseDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toUpdateCompilation(Compilation oldCompilation, CompilationDto newCompilation,
                                                                 List<Long> events) {
        return Compilation.builder()
                .id(oldCompilation.getId())
                .title(newCompilation.getTitle() != null ? newCompilation.getTitle() : oldCompilation.getTitle())
                .pinned(newCompilation.getPinned() != null ? newCompilation.getPinned() : oldCompilation.isPinned())
                .events(events)
                .build();
    }
}
