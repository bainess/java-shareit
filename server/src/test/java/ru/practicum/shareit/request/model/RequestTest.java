package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void shouldBeEqualWhenSameId() {
        Request r1 = Request.builder().id(1L).build();
        Request r2 = Request.builder().id(1L).build();

        assertEquals(r1, r2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Request r1 = Request.builder().id(1L).build();
        Request r2 = Request.builder().id(2L).build();

        assertNotEquals(r1, r2);
    }

    @Test
    void shouldBeEqualToItself() {
        Request r = Request.builder().id(1L).build();

        assertEquals(r, r);
    }

    @Test
    void shouldNotBeEqualToNull() {
        Request r = Request.builder().id(1L).build();

        assertNotEquals(null, r);
    }

    @Test
    void shouldBeEqualWhenBothIdsNull() {
        Request r1 = Request.builder().build();
        Request r2 = Request.builder().build();

        assertEquals(r1, r2);
    }

    @Test
    void hashCodeShouldBeEqualForSameId() {
        Request r1 = Request.builder().id(1L).build();
        Request r2 = Request.builder().id(1L).build();

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void hashCodeShouldBeConsistent() {
        Request r = Request.builder().id(1L).build();

        int h1 = r.hashCode();
        int h2 = r.hashCode();

        assertEquals(h1, h2);
    }
}