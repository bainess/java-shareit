package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.IllegalAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingService bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;
    private NewBookingRequest newBookingRequest;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@example.com")
                .build();

        booker = User.builder()
                .id(2L)
                .name("Booker")
                .email("booker@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(owner)
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        newBookingRequest = NewBookingRequest.builder()
                .itemId(1L)
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();
    }

    // ==================== TESTS FOR saveBooking ====================

    @Test
    void saveBooking_ShouldReturnBookingDto_WhenValidRequest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(1L);
            return savedBooking;
        });

        BookingDto result = bookingService.saveBooking(newBookingRequest, 2L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void saveBooking_ShouldThrowNotFoundException_WhenItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.saveBooking(newBookingRequest, 2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item 1 was not found");

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void saveBooking_ShouldThrowForbiddenException_WhenItemNotAvailable() {
        item.setAvailable(false);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.saveBooking(newBookingRequest, 2L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Item 1 is not available");

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void saveBooking_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.saveBooking(newBookingRequest, 2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User 2 was not found");

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    // ==================== TESTS FOR saveApproval ====================

    @Test
    void saveApproval_ShouldApproveBooking_WhenIsApprovedIsTrue() {
        when(bookingRepository.findByIdAndItem_Owner_Id(1L, 1L)).thenReturn(booking);

        BookingDto result = bookingService.saveApproval(1L, 1L, true);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void saveApproval_ShouldRejectBooking_WhenIsApprovedIsFalse() {
        when(bookingRepository.findByIdAndItem_Owner_Id(1L, 1L)).thenReturn(booking);

        BookingDto result = bookingService.saveApproval(1L, 1L, false);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Status.REJECTED);
        verify(bookingRepository, times(1)).save(booking);
    }

    // ==================== TESTS FOR checkBooking ====================

    @Test
    void checkBooking_ShouldReturnBookingDto_WhenUserIsBooker() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.checkBooking(1L, 2L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void checkBooking_ShouldReturnBookingDto_WhenUserIsOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.checkBooking(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void checkBooking_ShouldThrowIllegalAccessException_WhenUserHasNoAccess() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.checkBooking(1L, 999L))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining("does not have access");
    }

    @Test
    void checkBooking_ShouldThrowNotFoundException_WhenBookingNotFound() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.checkBooking(999L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Booking 999 was not found");
    }

    // ==================== TESTS FOR findBookingsByBookerAndState ====================

    @Test
    void findBookingsByBookerAndState_ShouldThrowIllegalAccessException_WhenUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.findBookingsByBookerAndState(999L, State.ALL, 0, 10))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining("User 999 does not exist");

        verify(bookingRepository, never()).findByBooker_IdOrderByStartDesc(anyLong(), any());
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void findBookingsByBookerAndState_ShouldHandleAllStates(State state) {
        when(userRepository.existsById(2L)).thenReturn(true);
        Pageable pageable = PageRequest.of(0, 10);
        List<Booking> bookings = List.of(booking);

        switch (state) {
            case CURRENT -> when(bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                    eq(2L), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                    .thenReturn(bookings);
            case PAST -> when(bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(
                    eq(2L), any(LocalDateTime.class), eq(pageable)))
                    .thenReturn(bookings);
            case FUTURE -> when(bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(
                    eq(2L), any(LocalDateTime.class), eq(pageable)))
                    .thenReturn(bookings);
            case WAITING -> when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(
                    eq(2L), eq(State.WAITING), eq(pageable)))
                    .thenReturn(bookings);
            case REJECTED -> when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(
                    eq(2L), eq(State.REJECTED), eq(pageable)))
                    .thenReturn(bookings);
            case ALL -> when(bookingRepository.findByBooker_IdOrderByStartDesc(eq(2L), eq(pageable)))
                    .thenReturn(bookings);
        }

        List<BookingDto> result = bookingService.findBookingsByBookerAndState(2L, state, 0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void findBookingsByBookerAndState_ShouldHandlePaginationCorrectly() {
        when(userRepository.existsById(2L)).thenReturn(true);
        Pageable expectedPageable = PageRequest.of(2, 20);

        when(bookingRepository.findByBooker_IdOrderByStartDesc(eq(2L), eq(expectedPageable)))
                .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.findBookingsByBookerAndState(2L, State.ALL, 40, 20);

        assertThat(result).hasSize(1);
        verify(bookingRepository).findByBooker_IdOrderByStartDesc(eq(2L), any(Pageable.class));
    }

    // ==================== TESTS FOR findBookingsByOwnerAndState ====================

    @Test
    void findBookingsByOwnerAndState_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.findBookingsByOwnerAndState(999L, State.ALL))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User 999 does not exist");

        verify(bookingRepository, never()).findByItem_Owner_IdOrderByStartDesc(anyLong());
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void findBookingsByOwnerAndState_ShouldHandleAllStates(State state) {
        when(userRepository.existsById(1L)).thenReturn(true);
        List<Booking> bookings = List.of(booking);

        switch (state) {
            case CURRENT -> when(bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                    eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(bookings);
            case PAST -> when(bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(
                    eq(1L), any(LocalDateTime.class)))
                    .thenReturn(bookings);
            case FUTURE -> when(bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(
                    eq(1L), any(LocalDateTime.class)))
                    .thenReturn(bookings);
            case WAITING -> when(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(
                    eq(1L), eq(State.WAITING)))
                    .thenReturn(bookings);
            case REJECTED -> when(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(
                    eq(1L), eq(State.REJECTED)))
                    .thenReturn(bookings);
            case ALL -> when(bookingRepository.findByItem_Owner_IdOrderByStartDesc(eq(1L)))
                    .thenReturn(bookings);
        }

        List<BookingDto> result = bookingService.findBookingsByOwnerAndState(1L, state);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void findBookingsByOwnerAndState_ShouldReturnEmptyList_WhenNoBookings() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByItem_Owner_IdOrderByStartDesc(1L)).thenReturn(List.of());

        List<BookingDto> result = bookingService.findBookingsByOwnerAndState(1L, State.ALL);

        assertThat(result).isEmpty();
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    void saveBooking_ShouldHandleBookingWithSameDayStartAndEnd() {
        newBookingRequest.setStart(now.plusDays(1));
        newBookingRequest.setEnd(now.plusDays(1));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.saveBooking(newBookingRequest, 2L);

        assertThat(result).isNotNull();
    }

    @Test
    void saveApproval_ShouldThrowNullPointerException_WhenBookingNotFound() {
        when(bookingRepository.findByIdAndItem_Owner_Id(999L, 1L)).thenReturn(null);

        assertThatThrownBy(() -> bookingService.saveApproval(1L, 999L, true))
                .isInstanceOf(NullPointerException.class);
    }
}