package ru.practicum.service.user;

import ru.practicum.dto.UserDto;
import ru.practicum.exception.ConflictDataException;
import ru.practicum.exception.IncorrectDataException;
import ru.practicum.exception.NoDataException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.UserMapper.toUser;
import static ru.practicum.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IncorrectDataException("Field: email. Error: must not be blank. Value: null");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IncorrectDataException("Field: name. Error: must not be blank. Value: null");
        }
        if (user.getName().length() < 2 || user.getName().length() > 250) {
            throw new IncorrectDataException("Field: name. Error: must be > 2 && < 250. Value: " + user.getName().length());
        }
        if (user.getEmail().length() < 6 || user.getEmail().length() > 254) {
            throw new IncorrectDataException("Field: email. Error: must be > 6 && < 254. Value: " + user.getEmail().length());
        }

        if (userRepository.findUserByName(user.getName()).isPresent()) {
            throw new ConflictDataException("Field: name. Error: name must be unique");
        }
         User userFromDb = userRepository.save(toUser(user));
        return toUserDto(userFromDb);
    }

    @Override
    public UserDto deleteUser(long userId) {
        User userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataException("User with id = " + userId + " was not found"));
        userRepository.delete(userFromDb);
        return toUserDto(userFromDb);
    }

    @Override
    public List<UserDto> getUsers(long[] ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<User> result;
        if (ids == null) {
            result = userRepository.getAllUsersWithPageable(page);
        } else {
            result = userRepository.getUsersByIdsWithPageable(ids, page);
        }
        return result.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
