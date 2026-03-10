package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto updateItem(Long itemId, Item item);

    List<ItemDto> findAllItemsByUser(Long userId);

    ItemDto saveItem(Item item);

    ItemDto getItemById(Long itemId);
}
