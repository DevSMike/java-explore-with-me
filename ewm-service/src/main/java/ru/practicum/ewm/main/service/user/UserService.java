package ru.practicum.ewm.main.service.user;

import ru.practicum.ewm.main.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user);

    UserDto deleteUser(long userId);

    List<UserDto> getUsers(long[] ids, int from, int size);
}
