package ru.practicum.service.category;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryInfo(long catId);
}
