package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithBookingDatesAndComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto saveComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable("itemId") Long itemId,
                                  @RequestBody NewCommentRequest comment) {
        return itemService.saveComment(userId, itemId, comment);
    }


    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        return itemService.searchItems(text);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoWithBookingDatesAndComments> getItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Request for items by user {}", userId);
        return itemService.findAllItemsByUser(userId);
    }


    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoWithBookingDatesAndComments getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @PathVariable(name = "itemId") Long itemId) {
        log.debug("request for item {}", itemId);
        return itemService.getItemById(userId, itemId);
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