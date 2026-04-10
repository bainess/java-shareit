package ru.practicum.shareit.item.dto.mapper;

import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithBookingDatesAndComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public final class ItemMapper {

    public static ItemDto itemToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static ItemDtoWithBookingDatesAndComments itemToDtoWithBookingDatesAndComments(Item item, LocalDateTime nextBookingDate, LocalDateTime lastBookingDate) {
        ItemDtoWithBookingDatesAndComments dto = new ItemDtoWithBookingDatesAndComments();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setNextBooking(nextBookingDate);
        dto.setLastBooking(lastBookingDate);
        return dto;
    }

    public static Item mapToItem(User user, NewItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        item.setOwner(user);
        return item;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }

        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }

        if (request.hasAvailability()) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }
}