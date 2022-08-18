package ru.otus.services;

import ru.otus.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserInfo(Long userId);

    Optional<User> getRandomUser();
}