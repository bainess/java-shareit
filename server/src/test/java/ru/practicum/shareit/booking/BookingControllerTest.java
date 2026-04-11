package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithBookingDatesAndComments;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetBooking() throws Exception {
        Long userId = 1L;
        Long bookingId = 10L;

        BookingDto dto = BookingDto.builder().id(bookingId).build();

        Mockito.when(bookingService.checkBooking(bookingId, userId))
                .thenReturn(dto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    void shouldApproveBooking() throws Exception {
        Long userId = 1L;
        Long bookingId = 10L;

        BookingDto dto = BookingDto.builder().id(bookingId).build();

        Mockito.when(bookingService.saveApproval(userId, bookingId, true))
                .thenReturn(dto);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        Long userId = 1L;

        NewBookingRequest request = new NewBookingRequest();
        request.setItemId(5L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto response = BookingDto.builder().id(100L).build();

        Mockito.when(bookingService.saveBooking(Mockito.any(), Mockito.eq(userId)))
                .thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));
    }

    @Test
    void shouldGetBookingsByBooker() throws Exception {
        Long userId = 1L;

        BookingDto dto = BookingDto.builder().id(1L).build();

        Mockito.when(bookingService.findBookingsByBookerAndState(
                        userId, State.ALL, 0, 10))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldGetBookingsByOwner() throws Exception {
        Long userId = 1L;

        BookingDto dto = BookingDto.builder().id(2L).build();

        Mockito.when(bookingService.findBookingsByOwnerAndState(userId, State.ALL))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    void shouldGetItemById() throws Exception {
        Long userId = 1L;
        Long itemId = 5L;

        ItemDtoWithBookingDatesAndComments item =
                ItemDtoWithBookingDatesAndComments.builder().id(itemId).build();

        Mockito.when(itemService.getItemById(userId, itemId))
                .thenReturn(item);

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));
    }

    @Test
    void shouldSearchItems() throws Exception {
        ItemDto item = ItemDto.builder().id(1L).name("item").build();

        Mockito.when(itemService.searchItems("text"))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldAddComment() throws Exception {
        Long userId = 1L;
        Long itemId = 3L;

        NewCommentRequest request = new NewCommentRequest();
        request.setText("Nice item");

        CommentDto response = CommentDto.builder().id(1L).text("Nice item").build();

        Mockito.when(itemService.saveComment(
                Mockito.eq(userId),
                Mockito.eq(itemId),
                Mockito.any(NewCommentRequest.class)
        )).thenReturn(response);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Nice item"));
    }
}