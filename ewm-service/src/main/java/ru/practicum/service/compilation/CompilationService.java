package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationResponseDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto createCompilation(CompilationDto compilation);

    CompilationResponseDto getCompilationById(long compId);

    List<CompilationResponseDto> getCompilationsPublic(boolean pinned, int size, int from);

    void deleteCompilation(long comId);

    CompilationResponseDto updateCompilation(long compId, CompilationDto compilation);
}
