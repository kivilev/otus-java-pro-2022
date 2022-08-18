package ru.otus.controllers.mappers;

import org.springframework.stereotype.Component;
import ru.otus.controllers.models.UserResponseDto;
import ru.otus.model.User;

@Component
public class UserDtoMapper {
    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getLogin(), user.getPassword());
    }
}
