package ru.practicum.shareit.item.dto.item;

import lombok.Getter;

@Getter
public class NewItemRequest {

    private String name;

    private String description;

    private Boolean available;
}
