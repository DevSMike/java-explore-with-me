package ru.practicum.ewm.main.mapper;

import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.model.User;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
