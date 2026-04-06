package ru.practicum.shareit.user.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldSaveUser() {
        NewUserRequest request = NewUserRequest.builder()
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        User user = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();

        Mockito
                .when(userRepository.existsByEmail("ivan@mail.ru"))
                .thenReturn(false);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto result = userService.saveUser(request);

        Assertions.assertEquals("ivan@mail.ru", result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenSaveUser() {
        NewUserRequest request = NewUserRequest.builder()
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        Mockito
                .when(userRepository.existsByEmail("ivan@mail.ru"))
                .thenReturn(true);

        Assertions.assertThrows(DuplicatedDataException.class, () -> userService.saveUser(request));
    }

    @Test
    void shouldUpdateUser() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Ivan")
                .email("ivan-ivanov@mail.ru")
                .build();

        User user = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();

        User savedUser = User.builder().id(1L).name("Ivan").email("ivan-ivanov@mail.ru").build();
        Mockito
                .when(userRepository.existsByEmail("ivan-ivanov@mail.ru"))
                .thenReturn(false);

        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto result = userService.updateUser(1L, request);

        Assertions.assertEquals("ivan-ivanov@mail.ru", result.getEmail());
    }

    @Test
    void shouldReturnUserById() {
        long userId = 1L;
        User user = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(userId);

        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getName(), result.getName());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenWrongId() {
        long userId = 1L;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("User not found"));

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void shouldConfirmMethodCalledWhenRemove() {
        long userId = 1L;

        Long result = userService.removeUser(userId);

        Mockito.verify(userRepository).deleteById(userId);

        Assertions.assertEquals(userId, result);
    }
}
