package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.dao.UserRepository;
import ru.otus.model.User;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserInfo(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getRandomUser() {
        return userRepository.findRandomUser();
    }
}