package ru.practicum.shareit.item.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void shouldSaveComment() {
        User user = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item description").comments(new ArrayList<>()).owner(user).build();
        NewCommentRequest comment = new NewCommentRequest();
        comment.setText("text of new comment");
        LocalDateTime time = LocalDateTime.now();

        Comment savedComment = Comment.builder().id(1L).author(user).created(time).text("text of new comment").build();

        Mockito
                .when(bookingRepository.existsByItem_IdAndBooker_IdAndEndAfter(org.mockito.Mockito.anyLong(), org.mockito.Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(false);
        Mockito
                .when(userRepository.findById(org.mockito.Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(commentRepository.save(Mockito.any()))
                .thenReturn(savedComment);

        CommentDto savedComment2 = itemService.saveComment(1L, 1L, comment);
        Assertions.assertEquals(savedComment2.getText(), savedComment.getText());
        Assertions.assertEquals(savedComment2.getAuthorName(), savedComment.getAuthor().getName());
    }

    @Test
    void shouldThrowExceptionWhenNoBooking() {
        NewCommentRequest comment = new NewCommentRequest();

        Mockito
                .when(bookingRepository.existsByItem_IdAndBooker_IdAndEndAfter(org.mockito.Mockito.anyLong(), org.mockito.Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(true);


        Assertions.assertThrows(IllegalAccessException.class, () -> itemService.saveComment(1L, 1L, comment));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotExists() {
        User user = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();
        NewCommentRequest comment = new NewCommentRequest();
        comment.setText("text of new comment");
        LocalDateTime time = LocalDateTime.now();

        Comment savedComment = Comment.builder().id(1L).author(user).created(time).text("text of new comment").build();

        Mockito
                .when(bookingRepository.existsByItem_IdAndBooker_IdAndEndAfter(org.mockito.Mockito.anyLong(), org.mockito.Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(false);
        Mockito
                .when(userRepository.findById(org.mockito.Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.saveComment(1L, 1L, comment));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotExists() {
        User user = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();
        User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();
        NewCommentRequest comment = new NewCommentRequest();
        comment.setText("text of new comment");
        LocalDateTime time = LocalDateTime.now();

        Comment savedComment = Comment.builder().id(1L).author(user).created(time).text("text of new comment").build();

        Mockito
                .when(bookingRepository.existsByItem_IdAndBooker_IdAndEndAfter(org.mockito.Mockito.anyLong(), org.mockito.Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(false);
        Mockito
                .when(userRepository.findById(org.mockito.Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.saveComment(1L, 1L, comment));
    }

    @Test
    void shouldReturnItems() {
        Long userId = 1L;

        User user = User.builder().id(userId).build();

        Item item = Item.builder().id(1L).owner(user).available(true).comments(new ArrayList<>()).build();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastEnd = now.minusDays(1);
        LocalDateTime nextEnd = now.plusDays(1);

        Booking lastBook = Booking.builder().end(lastEnd).build();
        Booking nextBook = Booking.builder().end(nextEnd).build();

        Mockito.when(itemRepository.findByOwnerId(userId))
                .thenReturn(List.of(item));

        Mockito
                .when(bookingRepository.findFirstByItem_IdAndEndAfterOrderByEndDesc(Mockito.eq(item.getId()), Mockito.any()))
                .thenReturn(Optional.of(lastBook));

        Mockito.when(bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(Mockito.eq(item.getId()), Mockito.any()))
                .thenReturn(Optional.of(nextBook));

        List<ItemDtoWithBookingDatesAndComments> result = itemService.findAllItemsByUser(userId);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(nextEnd, result.getFirst().getLastBooking());
        Assertions.assertEquals(lastEnd, result.getFirst().getNextBooking());

    }

    @Test
    void shouldThrowExceptionWhenSaveItemAndUserNotFound() {
        Long userId = null;
        NewItemRequest request = new NewItemRequest();
        Assertions.assertThrows(NotFoundException.class, () -> itemService.saveItem(userId, request));
    }

    @Test
    void shouldThrowExceptionWhenSaveItemAndAvailableNull() {
        Long userId = 1L;
        NewItemRequest request = NewItemRequest.builder().name("item").description("item description").build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> itemService.saveItem(userId, request));
    }

    @Test
    void shouldThrowExceptionWhenSaveItemAndRequestNotExists() {
        Long userId = 1L;
        NewItemRequest request = NewItemRequest.builder().name("item").description("item description").available(true).requestId(2L).build();

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.saveItem(userId, request));
    }

    @Test
    void shouldThrowExceptionWhenRequestNotFound() {
        Long userId = 1L;
        NewItemRequest request = NewItemRequest.builder().name("item").description("item description").available(true).requestId(3L).build();
        User user = User.builder().id(4L).name("ivan").build();

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.saveItem(userId, request));
    }

    @Test
    void shouldSaveItem() {
        Long userId = 1L;
        Long requestId = 2L;
        NewItemRequest request = NewItemRequest.builder().name("item").description("item description").available(true).requestId(3L).build();
        User user = User.builder().id(4L).name("ivan").build();
        Request requestItem = Request.builder().id(requestId).description("item description").requestor(user).build();
        Item item = Item.builder().id(6L).name("item").description("item description").available(true).request(requestItem).build();

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(requestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(requestItem));

        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(item);

        ItemDto result = itemService.saveItem(userId, request);

        Assertions.assertEquals(6L, result.getId());
        Assertions.assertEquals("item", result.getName());
        Assertions.assertEquals(request.getDescription(), result.getDescription());
        Assertions.assertTrue(result.isAvailable());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        Long userId = 1L;
        Long itemId = 2L;
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(userId, itemId, updateRequest));
    }

    @Test
    void shouldThrowNotFoundExceptionWheItemNotFound() {
        Long userId = 1L;
        Long itemId = 2L;
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        User user = User.builder().id(4L).name("ivan").build();

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(userId, itemId, updateRequest));
    }

    @Test
    void shouldUpdateItem() {
        Long userId = 1L;
        Long itemId = 2L;
        LocalDateTime bookedFrom = LocalDateTime.now();
        LocalDateTime bookedTo = LocalDateTime.now().plusDays(1);
        Item item = Item.builder().id(6L).name("item").description("item description").available(true).build();
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .name("tool")
                .description("new tool description")
                .available(false)
                .bookedFrom(bookedFrom)
                .bookedTo(bookedTo)
                .build();
        User user = User.builder().id(4L).name("ivan").build();

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        ItemDto result = itemService.updateItem(userId, itemId, updateRequest);
        Assertions.assertEquals(updateRequest.getName(), result.getName());
        Assertions.assertEquals(updateRequest.getDescription(), result.getDescription());
        Assertions.assertEquals(updateRequest.getAvailable(), result.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenGetItemAndItemNotExists() {
        Long itemId = 1L;
        Long userId = 2L;
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(userId, itemId));
    }

    @Test
    void shouldReturnItem() {
        Long itemId = 1L;
        Long userId = 2L;
        LocalDateTime now = LocalDateTime.now();
        Item item = Item.builder().id(itemId).name("item").description("item description").available(true).build();
        Booking lastBooking = Booking.builder().start(now.minusDays(2)).end(now.minusDays(1)).build();
        Booking nextBooking = Booking.builder().start(now.plusDays(1)).end(now.plusDays(2)).build();
        Comment comment = new Comment();
        comment.setText("text of new comment");
        User author = User.builder().id(1L).name("Author Name").build();
        comment.setAuthor(author);
        comment.setItem(item);

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(bookingRepository.findFirstByItem_IdAndEndAfterOrderByEndDesc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Optional.of(lastBooking));

        Mockito.when(bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Optional.of(nextBooking));

        Mockito.when(commentRepository.findAllByItem_Id(Mockito.anyLong()))
                .thenReturn(List.of(comment));

        ItemDtoWithBookingDatesAndComments result = itemService.getItemById(userId, itemId);

        Assertions.assertEquals(itemId, result.getId());
        Assertions.assertEquals(item.getName(), result.getName());
        Assertions.assertEquals(item.getDescription(), result.getDescription());
        Assertions.assertEquals(lastBooking.getEnd(), result.getLastBooking());
    }


}
