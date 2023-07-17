package ru.practicum.ewm.main.mapper;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.model.Category;

public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        return  Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static Category toCategoryUpdate(CategoryDto newCat, Category oldCat) {
        if (newCat.getName().isEmpty()) {
            newCat.setName(null);
        }
        return Category.builder()
                .id(newCat.getId())
                .name(newCat.getName() != null ? newCat.getName() : oldCat.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
