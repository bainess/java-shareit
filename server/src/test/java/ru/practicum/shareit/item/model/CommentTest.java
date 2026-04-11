package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CommentTest {

    @Test
    void shouldBeEqualWhenIdsMatch() {
        Comment c1 = Comment.builder().id(1L).build();
        Comment c2 = Comment.builder().id(1L).build();

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsDifferent() {
        Comment c1 = Comment.builder().id(1L).build();
        Comment c2 = Comment.builder().id(2L).build();

        assertNotEquals(c1, c2);
    }

    @Test
    void shouldNotBeEqualWhenIdNull() {
        Comment c1 = Comment.builder().id(null).build();
        Comment c2 = Comment.builder().id(null).build();

        assertNotEquals(c1, c2);
    }
}