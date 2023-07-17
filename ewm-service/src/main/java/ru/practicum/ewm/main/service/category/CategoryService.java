package ru.practicum.ewm.main.service.category;

import ru.practicum.ewm.main.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryInfo(long catId);
}
