package ru.practicum.ewm.main.controller.adminApi;

import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.main.service.compilation.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin")
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto  createCompilation(@RequestBody CompilationDto compilation) {
        log.debug("Admin: create compilation");
        return compilationService.createCompilation(compilation);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Min(0) long compId) {
        log.debug("Admin: deleting compilation with id: {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationResponseDto updateCompilation(@PathVariable @Min(0) long compId,
                                                    @RequestBody CompilationDto compilation) {
        log.debug("Admin: updating compilation with id: {}", compId);
        return compilationService.updateCompilation(compId, compilation);
    }
}
