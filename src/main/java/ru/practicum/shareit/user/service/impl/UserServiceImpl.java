package ru.practicum.shareit.user.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDto saveUser(NewUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedDataException("This email is registered");
        }

        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        return UserMapper.maptoUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedDataException("This email is registered");
        }

        User updatedUser = userRepository.findById(userId)
                .map(user -> UserMapper.fieldsToUpdate(user,request))
                .orElseThrow(() -> new NotFoundException("User was not found"));
        return UserMapper.maptoUserDto(updatedUser);
    }

    @Override
    public Long removeUser(Long userId) {
        userRepository.deleteById(userId);
        return userId;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.maptoUserDto(user);
    }
}
