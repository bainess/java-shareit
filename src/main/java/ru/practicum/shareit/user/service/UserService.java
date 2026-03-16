package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto saveUser(NewUserRequest user);

    UserDto updateUser(Long userId, UpdateUserRequest user);

    Long removeUser(Long userId);

    UserDto getUserById(Long userId);
}
