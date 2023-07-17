package ru.practicum.ewm.main.controller.adminApi;

import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin")
public class UserControllerAdmin {

    private final UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.debug("Admin: creating user: {}", userDto);
        return userService.addUser(userDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) long[] ids, @RequestParam(defaultValue = "0") @Min(0) int from,
                                  @RequestParam(defaultValue = "10") @Min(0) int size) {
        log.debug("Admin: getting users");
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto deleteUser(@PathVariable long userId) {
        log.debug("Admin: deleting user with id: {}", userId);
        return userService.deleteUser(userId);
    }
}
