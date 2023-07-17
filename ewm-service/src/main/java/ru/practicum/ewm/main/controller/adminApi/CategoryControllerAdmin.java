package ru.practicum.ewm.main.controller.adminApi;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin")
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        log.debug("Admin: creating category: {}", categoryDto);
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId, @RequestBody CategoryDto categoryDto) {
        log.debug("Admin: updating category with id: {}", catId);
        categoryDto.setId(catId);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.debug("Admin: deleting category with id: {}", catId);
        categoryService.deleteCategory(catId);
    }
}
