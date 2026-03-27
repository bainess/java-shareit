package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<ItemDto> findAllItemsByUser(Long userId) {
        return itemRepository.findByOwnerId(userId).stream().map(ItemMapper::itemToDto).toList();
    }

    public ItemDto updateItem(Long userId, Long itemId, UpdateItemRequest request) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + "was not found"));

        Item updatedItem = itemRepository.findById(itemId)
                .map(item -> ItemMapper.updateItemFields(item, request))
                .orElseThrow(() -> new NotFoundException("Item was not found"));
        itemRepository.save(updatedItem);
        return ItemMapper.itemToDto(updatedItem);
    }

    public ItemDto saveItem(Long userId, NewItemRequest request) {
        if (userId == null) {
            throw new NotFoundException("User does not exist");
        }
        if (request.getAvailable() == null) {
            throw new IllegalArgumentException("Availability should be set");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + "was not found"));
        Item item = ItemMapper.mapToItem(user, request);

                itemRepository.save(item);
        return ItemMapper.itemToDto(item);
    }

    public ItemDto getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemMapper::itemToDto)
                .orElseThrow(() -> new NotFoundException("Item was not found"));
    }


    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {return new ArrayList<>(); }
        List<ItemDto> itemsFound = itemRepository
                .findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(
                        text, text)
                .stream()
                .map(ItemMapper::itemToDto)
                .toList();

        if (itemsFound .isEmpty()) {
           return new ArrayList<>();
        }

        return itemsFound;

    }
}
