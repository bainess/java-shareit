package ru.practicum.shareit.item.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dal.ItemInMemoryRepository;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemInMemoryRepository itemRepository;

    public ItemServiceImpl(ItemInMemoryRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDto> findAllItemsByUser(Long userId) {
        return itemRepository.getAllItemsByUser(userId).stream().map(ItemMapper::itemToDto).toList();
    }

    public ItemDto updateItem(Long itemId, UpdateItemRequest request) {
        Item updatedItem = itemRepository.getItem(itemId)
                .map(item -> ItemMapper.updateItemFields(item, request))
                .orElseThrow(() -> new NotFoundException("Item was not found"));
        itemRepository.updateItem(updatedItem);
        return ItemMapper.itemToDto(updatedItem);
    }

    public ItemDto saveItem(Item item) {
        Item newItem = itemRepository.saveItem(item);
        return ItemMapper.itemToDto(item);
    }

    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItem(itemId)
                .map(ItemMapper::itemToDto)
                .orElseThrow(() -> new NotFoundException("Item was not found"));
    }

    public List<ItemDto> searchItems(String text) {
        List<ItemDto> itemsFound = itemRepository.searchForItem(text).stream()
                .map(ItemMapper::itemToDto)
                .toList();

        if (itemsFound == null) {
            new NotFoundException("No item matching description was found");
        };

        return itemsFound;

    }
}
