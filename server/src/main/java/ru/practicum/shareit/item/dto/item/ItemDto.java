package ru.practicum.shareit.item.dto.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.model.Request;

@Setter
@Getter
@RequiredArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private Request request;
}

