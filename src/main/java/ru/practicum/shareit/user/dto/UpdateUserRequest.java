package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private Long id;
    private String email;
    private String name;

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }
}
