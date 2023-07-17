package ru.practicum.ewm.main.repository;

import ru.practicum.ewm.main.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select c from Compilation c where c.pinned = :pinned")
    List<Compilation> getCompilations(boolean pinned, Pageable page);
}
