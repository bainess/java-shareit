package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dal.ItemInMemoryRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemInMemoryRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(ItemInMemoryRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public List<ItemDto> findAllItemsByUser(Long userId) {
        return itemRepository.getAllItemsByUser(userId).stream().map(ItemMapper::itemToDto).toList();
    }

    public ItemDto updateItem(Long userId, Long itemId, UpdateItemRequest request) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("User does not exist");
        }

        Item updatedItem = itemRepository.getItem(itemId)
                .map(item -> ItemMapper.updateItemFields(item, request))
                .orElseThrow(() -> new NotFoundException("Item was not found"));
        itemRepository.updateItem(updatedItem);
        return ItemMapper.itemToDto(updatedItem);
    }

    public ItemDto saveItem(Long userId, NewItemRequest request) {
        if (userId == null) {
            throw new NotFoundException("User does not exist");
        }
        if (request.getAvailable() == null) {
            throw new IllegalArgumentException("Availability should be set");
        }

        userService.getUserById(userId);

        Item item = ItemMapper.mapToItem(userId, request);
                itemRepository.saveItem(userId, item);
        return ItemMapper.itemToDto(item);
    }

    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItem(itemId)
                .map(ItemMapper::itemToDto)
                .orElseThrow(() -> new NotFoundException("Item was not found"));
    }


    public List<ItemDto> searchItems(String text) {
        List<ItemDto> itemsFound = itemRepository.searchForItem(text.toLowerCase()).stream()
                .map(ItemMapper::itemToDto)
                .toList();

        if (itemsFound == null) {
            throw new NotFoundException("No item matching description was found");
        }

        return itemsFound;

    }
}
