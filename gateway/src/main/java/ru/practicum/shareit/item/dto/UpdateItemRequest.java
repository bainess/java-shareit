package ru.practicum.shareit.item.dto;

import java.time.LocalDate;

public class UpdateItemRequest {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private LocalDate bookedFrom;
    private LocalDate bookedTo;
}
