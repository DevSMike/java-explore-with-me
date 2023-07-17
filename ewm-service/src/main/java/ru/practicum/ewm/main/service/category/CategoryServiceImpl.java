package ru.practicum.ewm.main.service.category;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.CategoryMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isBlank()) {
            throw new IncorrectDataException("Field: name. Error: must not be blank. Value: null");
        }
        checkNameLength(categoryDto.getName().length());

        Category catWithName = categoryRepository.findCategoryByName(categoryDto.getName()).orElse(null);
        if (catWithName != null) {
            throw new ConflictDataException("Field: name. Name must be unique");
        }
        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        if (categoryDto.getName() == null) {
            categoryDto.setName("");
        }
        checkNameLength(categoryDto.getName().length());
        Category catWithName = categoryRepository.findCategoryByName(categoryDto.getName()).orElse(null);
        if (catWithName != null && catWithName.getId() != categoryDto.getId()) {
            throw new ConflictDataException("Field: name. Name must be unique");
        }
        Category catFromDb = findCategory(categoryDto.getId());
        Category newCategory = CategoryMapper.toCategoryUpdate(categoryDto, catFromDb);
        categoryRepository.save(newCategory);
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    public void deleteCategory(long catId) {
        findCategory(catId);
        List<Event> eventsWithCategory = eventRepository.findEventByCategory(catId);
        if (eventsWithCategory.size() > 0) {
            throw new ConflictDataException("Filed: category. Can't delete the category with events");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryRepository.getAllCategoriesWithPageable(page)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryInfo(long catId) {
        Category catFromDb = findCategory(catId);
        return CategoryMapper.toCategoryDto(catFromDb);
    }

    private Category findCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoDataException("Category with id = " + id + " was not found"));
    }

    private void checkNameLength(long len) {
        if (len > 50) {
            throw new IncorrectDataException("Field: name. Error: length must be < 50. Value: " + len);
        }
    }
}
