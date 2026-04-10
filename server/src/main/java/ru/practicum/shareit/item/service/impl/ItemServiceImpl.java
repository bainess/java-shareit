package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IllegalAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithBookingDatesAndComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemRequest;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import java.util.List;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    public CommentDto saveComment(Long userId, Long itemId, NewCommentRequest request) {
        log.debug(request.getText());
        LocalDateTime created = LocalDateTime.now();
        if (bookingRepository.existsByItem_IdAndBooker_IdAndEndAfter(itemId, userId, created)) {
            throw new IllegalAccessException(" User" + userId + " did not book the ru.practicum.shareit.ru.practicum.shareit.item" + itemId);
        }

        Comment comment = CommentMapper.mapToComment(request);
        User author = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item " + itemId + " was not found"));

        comment.setCreated(created);
        comment.setAuthor(author);
        comment.setItem(item);
        item.addComment(comment);

        commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    public List<ItemDtoWithBookingDatesAndComments> findAllItemsByUser(Long userId) {

        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> {
                    LocalDateTime now = LocalDateTime.now();
                    Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndAfterOrderByEndDesc(item.getId(), now).orElse(new Booking());
                    Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(item.getId(), now).orElse(new Booking());
                    log.debug("next ru.practicum.shareit.ru.practicum.shareit.booking{}", nextBooking);
                    return ItemMapper.itemToDtoWithBookingDatesAndComments(item, lastBooking.getEnd(), nextBooking.getEnd());
                })
                .toList();
    }

    public ItemDto updateItem(Long userId, Long itemId, UpdateItemRequest request) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + "was not found"));

        Item updatedItem = itemRepository.findById(itemId)
                .map(item -> ItemMapper.updateItemFields(item, request))
                .orElseThrow(() -> new NotFoundException("Item was not found"));
        itemRepository.save(updatedItem);
        return ItemMapper.itemToDto(updatedItem);
    }

    public ItemDto saveItem(Long userId, NewItemRequest itemRequest) {
        if (userId == null) {
            throw new NotFoundException("User does not exist");
        }
        if (itemRequest.getAvailable() == null) {
            throw new IllegalArgumentException("Availability should be set");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + "was not found"));

        Item item = ItemMapper.mapToItem(user, itemRequest);

        if (itemRequest.getRequestId() != null) {
            log.debug("Getting item with request {}", requestRepository.findById(itemRequest.getRequestId()));
            Request request = requestRepository.findById(itemRequest.getRequestId()).orElseThrow(() -> new NotFoundException("Request id " + itemRequest.getRequestId()+ " not found"));
            item.setRequest(request);
        }

        item = itemRepository.save(item);
        return ItemMapper.itemToDto(item);
    }

    public ItemDtoWithBookingDatesAndComments getItemById(Long userId, Long itemId) {
        LocalDateTime now = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item was not found"));
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndAfterOrderByEndDesc(item.getId(), now)
                .orElse(new Booking());
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(item.getId(), now)
                .orElse(new Booking());


        List<CommentDto> comments = commentRepository.findAllByItem_Id(itemId).stream().map(CommentMapper::mapToCommentDto).toList();
        ItemDtoWithBookingDatesAndComments dto =
                ItemMapper.itemToDtoWithBookingDatesAndComments(item, nextBooking.getEnd(), lastBooking.getEnd());
        dto.setComments(comments);
        return dto;

    }


    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> itemsFound = itemRepository
                .findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(
                        text, text)
                .stream()
                .map(ItemMapper::itemToDto)
                .toList();

        if (itemsFound.isEmpty()) {
            return new ArrayList<>();
        }

        return itemsFound;

    }
}
