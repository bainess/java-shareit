package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto updateItem(Long userId, Long itemId, UpdateItemRequest request);

    List<ItemDto> findAllItemsByUser(Long userId);

    ItemDto saveItem(Long userId, NewItemRequest item);

    ItemDto getItemById(Long itemId);

    List<ItemDto> searchItems(String text);
}
