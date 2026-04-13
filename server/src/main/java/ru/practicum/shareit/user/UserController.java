package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserRequest request) {
        UserDto user = userService.saveUser(request);
        log.info("Created user.id {}", user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequest request,
                                              @PathVariable(name = "userId") Long userId) {
        UserDto user = userService.updateUser(userId, request);
        log.info("Updated ru.practicum.shareit.ru.practicum.shareit.user.id: {}", request.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public Long deleteUser(@PathVariable(name = "userId") Long userId) {
        return userService.removeUser(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable(name = "userId") Long userId) {
        log.debug("Get user request: userId {}", userId);
        return userService.getUserById(userId);
    }
}
