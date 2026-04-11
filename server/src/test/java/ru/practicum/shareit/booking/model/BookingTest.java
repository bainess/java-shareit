package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookingTest {

    @Test
    void shouldBeEqualWhenIdsAreSame() {
        Booking b1 = Booking.builder().id(1L).build();
        Booking b2 = Booking.builder().id(1L).build();

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsDifferent() {
        Booking b1 = Booking.builder().id(1L).build();
        Booking b2 = Booking.builder().id(2L).build();

        assertNotEquals(b1, b2);
    }

    @Test
    void shouldNotBeEqualWhenIdIsNull() {
        Booking b1 = Booking.builder().id(null).build();
        Booking b2 = Booking.builder().id(null).build();

        assertNotEquals(b1, b2);
    }
}