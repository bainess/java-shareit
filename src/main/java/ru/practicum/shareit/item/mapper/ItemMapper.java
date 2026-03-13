package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

public final class ItemMapper {
    public static ItemDto itemToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }

        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }

        if (request.isAvailable()) {
            item.setAvailable(request.isAvailable());
        }

        if (request.hasBookedFrom()) {
            item.setBookedFrom(request.getBookedFrom());
        }

        if (request.hasBookedTo()) {
            item.setBookedTo(request.getBookedTo());
        }

        return item;
    }
}