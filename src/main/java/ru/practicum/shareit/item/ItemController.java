package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        return itemService.searchItems(text);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItemsByUser(userId);
    }



    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable(name = "itemId") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                        @Valid @RequestBody NewItemRequest item) {
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody UpdateItemRequest item,
                              @PathVariable(name = "itemId") Long itemId) {
        return itemService.updateItem(userId, itemId, item);
    }
}