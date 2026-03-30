package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NewUserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;
}
