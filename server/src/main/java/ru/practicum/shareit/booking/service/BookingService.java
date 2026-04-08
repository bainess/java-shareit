package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.exception.IllegalAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<BookingDto> findBookingsByBookerAndState(Long userId, State state, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalAccessException("User " + userId + " does not exist");
        }
        LocalDateTime now = LocalDateTime.now();

        Pageable pageable = PageRequest.of(from / size, size);

        log.info("user id {} requests {}", userId, state);
        return switch (state) {
            case CURRENT -> bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now, pageable)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case PAST -> bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, now, pageable)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case FUTURE -> bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, now, pageable)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case WAITING -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, State.WAITING, pageable)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case REJECTED -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, State.REJECTED, pageable)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case ALL -> bookingRepository.findByBooker_IdOrderByStartDesc(userId, pageable)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
        };
    }

    public List<BookingDto> findBookingsByOwnerAndState(Long userId, State state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User " + userId + " does not exist");
        }
        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case CURRENT ->
                    bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now)
                            .stream().map(BookingMapper::mapToBookingDto).toList();
            case PAST -> bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case FUTURE -> bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case WAITING -> bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId, State.WAITING)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case REJECTED -> bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId, State.REJECTED)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
            case ALL -> bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId)
                    .stream().map(BookingMapper::mapToBookingDto).toList();
        };
    }

    public BookingDto checkBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking " + bookingId + " was not found"));

        if (!Objects.equals(userId, booking.getBooker().getId())
                && !Objects.equals(userId, booking.getItem().getOwner().getId())) {
            throw new IllegalAccessException("User " + userId + "   does not have access to the ru.practicum.shareit.ru.practicum.shareit.booking");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    public BookingDto saveBooking(NewBookingRequest newBooking, Long userId) {
        Item item = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException("Item " + newBooking.getItemId() + " was not found"));

        if (!item.getAvailable()) {
            throw new ForbiddenException("Item " + item.getId() + " is not available");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " was not found"));


        Booking booking = BookingMapper.mapToBooking(booker, item, newBooking);
        booking.setStatus(Status.WAITING);

        bookingRepository.save(booking);

        log.debug(booking.toString());
        return BookingMapper.mapToBookingDto(booking);
    }

    public BookingDto saveApproval(Long userId, Long bookingId, boolean isApproved) {
        log.debug("Service approves user id {}, booking id{}, booking - {}", userId, bookingId, isApproved);

        Booking booking = bookingRepository.findByIdAndItem_Owner_Id(bookingId, userId);
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }
}
