package ru.otus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.controllers.mappers.UserDtoMapper;
import ru.otus.controllers.models.UserResponseDto;
import ru.otus.services.UserService;

import static ru.otus.controllers.errors.ErrorMessages.ERROR_MESSAGE_USER_NOT_FOUND;

@RestController
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @GetMapping("/api/user/{userId}")
    public UserResponseDto getUserInfo(@PathVariable Long userId) {
        var client = userService.getUserInfo(userId);

        if (client.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MESSAGE_USER_NOT_FOUND);

        return userDtoMapper.toUserResponseDto(client.get());
    }

}
