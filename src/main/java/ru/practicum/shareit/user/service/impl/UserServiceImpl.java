package ru.practicum.shareit.user.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DublicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Override
    public UserDto saveUser(User user) {
        if (userRepository.getEmails().contains(user.getEmail())) {
            throw new DublicatedDataException("This email is registered");
        }

        User newUser = userRepository.saveUser(user);
        return UserMapper.maptoUserDto(newUser);
    }

    @Override
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        if (userRepository.getEmails().contains(request.getEmail())) {
            throw new DublicatedDataException("This email is registered");
        }

        User updatedUser = userRepository.getUserById(userId)
                .map(user -> UserMapper.fieldsToUpdate(user,request))
                .orElseThrow(() -> new NotFoundException("User was not found"));
        return UserMapper.maptoUserDto(updatedUser);
    }

    @Override
    public Long removeUser(Long userId) {
        userRepository.deleteUser(userId);
        return userId;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.maptoUserDto(user);
    }
}
