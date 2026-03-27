package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.sbom.SbomEndpointAutoConfiguration;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
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

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto checkBooking(Long  bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking " + bookingId + " was not found"));
        log.debug(booking.toString());

        if (!Objects.equals(userId, booking.getBooker().getId())) {
            throw new IllegalAccessException("User " + userId + "   does not have access to the booking");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    public BookingDto saveBooking(NewBookingRequest newBooking, Long userId) {
        Item item = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException( "Item "+ newBooking.getItemId() + " was not found"));

        if (!item.getAvailable()) {throw new ForbiddenException("Item " + item.getId() + " is not available");}

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User " + userId + " was not found"));


        Booking booking = BookingMapper.mapToBooking(booker, item, newBooking);
        booking.setStatus(Status.WAITING);

        bookingRepository.save(booking);

        log.debug(booking.toString());
        return BookingMapper.mapToBookingDto(booking);
    }

    public List<BookingDto> findAllBookingByUser(Long userId, State state) {

    }
}
