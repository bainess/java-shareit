package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody User request) {
        UserDto user = userService.saveUser(request);
        log.info("Created user.id {}", user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserRequest request,
                                              @PathVariable(name="userId") Long userId) {
        UserDto user = userService.updateUser(userId, request);
        log.info("Updated user.id: {}", request.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public Long deleteUser(@PathVariable(name="userId") Long userId) {
        return userService.removeUser(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable(name="userId") Long userId) {
        return userService.getUserById(userId);
    }
}
