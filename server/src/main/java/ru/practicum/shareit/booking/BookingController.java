package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable(name = "bookingId") Long bookingId) {
        log.debug("Request ru.practicum.shareit.ru.practicum.shareit.booking {} by {}", bookingId, userId);
        return bookingService.checkBooking(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(name = "bookingId") Long bookingId,
                                     @RequestParam("approved") boolean isApproved) {
        log.debug("User {} approve booking {} - {}", userId, bookingId, isApproved);
        BookingDto dto = bookingService.saveApproval(userId, bookingId, isApproved);
        return dto;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingDto saveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody NewBookingRequest newbooking) {
        log.debug("Booking request {}", newbooking.toString());
        return bookingService.saveBooking(newbooking, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") State state,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        return bookingService.findBookingsByBookerAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.findBookingsByOwnerAndState(userId, state);
    }
}
