package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.HttpExchange;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.net.http.HttpRequest;
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
        log.debug("Request booking {} by {}", bookingId, userId);
        return bookingService.checkBooking(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable(name = "bookingId") Long bookingId,
                                     @RequestParam("approved") boolean isApproved) {
        return bookingService.saveApproval(userId, bookingId, isApproved);
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
                                                 @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.findBookingsByBookerAndState(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") State state) {
        return bookingService.findBookingsByOwnerAndState(userId, state);
    }
}
