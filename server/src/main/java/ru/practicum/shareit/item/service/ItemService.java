package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithBookingDatesAndComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;


import java.util.List;

public interface ItemService {
    ItemDto updateItem(Long userId, Long itemId, UpdateItemRequest request);

    List<ItemDtoWithBookingDatesAndComments> findAllItemsByUser(Long userId);

    ItemDto saveItem(Long userId, NewItemRequest item);

    ItemDtoWithBookingDatesAndComments getItemById(Long userId, Long itemId);

    List<ItemDto> searchItems(String text);

    CommentDto saveComment(Long userId, Long itemId, NewCommentRequest comment);
}
