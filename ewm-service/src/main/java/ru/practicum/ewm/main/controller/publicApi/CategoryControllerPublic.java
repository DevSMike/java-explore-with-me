package ru.practicum.ewm.main.controller.publicApi;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.CategoryService;
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
@RequestMapping("/categories")
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) int from,
                                           @RequestParam(defaultValue = "10") @Min(0) int size) {
        log.debug("Public: getting categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Min(0) long catId) {
        log.debug("Public: getting category by id: {}", catId);
        return categoryService.getCategoryInfo(catId);
    }
}
