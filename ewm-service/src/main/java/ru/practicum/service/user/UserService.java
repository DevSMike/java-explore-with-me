package ru.practicum.service.user;

import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user);

    UserDto deleteUser(long userId);

    List<UserDto> getUsers(long[] ids, int from, int size);
}
