package ru.otus.controllers.models;

public class UserResponseDto {
    private final long id;
    private final String name;
    private final String login;
    private final String password;

    public UserResponseDto(long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
