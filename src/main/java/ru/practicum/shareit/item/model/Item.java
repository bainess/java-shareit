package ru.practicum.shareit.item.model;

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
    private User owner;
    private String name;
    private String description;
    private boolean isAvailable;
    private LocalDate bookedFrom;
    private LocalDate bookedTo;
}