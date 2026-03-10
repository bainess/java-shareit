package ru.practicum.shareit.user.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 1L;

    public Optional<User> getUserById(Long userId) {
        return Optional.of(users.get(userId));
    }

    public User saveUser(User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    public Optional<User> updateUser(Long userId,User user) {
        if (!users.containsKey(userId)) {
            return Optional.empty();
        }
        user.setId(userId);
        users.put(userId, user);
        return Optional.of(user);
    }
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    public List<String> getEmails() {
        return users.values().stream().map(User::getEmail).toList();
    }
}