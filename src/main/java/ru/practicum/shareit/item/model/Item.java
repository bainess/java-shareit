package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
public class Item {
    private Long id;
    private Long owner;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;
    private LocalDate bookedFrom;
    private LocalDate bookedTo;
}