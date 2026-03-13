package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto updateItem(Long itemId, UpdateItemRequest request);

    List<ItemDto> findAllItemsByUser(Long userId);

    ItemDto saveItem(Item item);

    ItemDto getItemById(Long itemId);

    ItemDto searchItem(String text);
}
