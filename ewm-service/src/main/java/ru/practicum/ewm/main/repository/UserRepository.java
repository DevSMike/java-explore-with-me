package ru.practicum.ewm.main.repository;

import ru.practicum.ewm.main.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id IN :ids")
    List<User> getUsersByIdsWithPageable(long[] ids, Pageable page);

    @Query("select u from User u")
    List<User> getAllUsersWithPageable(Pageable page);

    Optional<User> findUserByName(String name);
}
