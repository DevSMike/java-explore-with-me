package ru.practicum.repository;

import ru.practicum.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c")
    List<Category> getAllCategoriesWithPageable(Pageable page);

    Optional<Category> findCategoryByName(String name);
}
