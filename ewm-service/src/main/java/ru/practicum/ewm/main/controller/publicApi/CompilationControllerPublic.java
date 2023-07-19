package ru.practicum.ewm.main.controller.publicApi;

import ru.practicum.ewm.main.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.main.service.compilation.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/compilations")
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompilation(@PathVariable @Min(0) long compId) {
        log.debug("Public: get compilation by id: {}", compId);
        return compilationService.getCompilationById(compId);
    }

    @GetMapping()
    public List<CompilationResponseDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.debug("Public: get compilation");
        return compilationService.getCompilationsPublic(pinned, size, from);
    }
}
