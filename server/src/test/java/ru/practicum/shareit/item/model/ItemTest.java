package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemTest {

    @Test
    void shouldBeEqualWhenIdsMatch() {
        Item i1 = Item.builder().id(1L).build();
        Item i2 = Item.builder().id(1L).build();

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsDifferent() {
        Item i1 = Item.builder().id(1L).build();
        Item i2 = Item.builder().id(2L).build();

        assertNotEquals(i1, i2);
    }

    @Test
    void shouldNotBeEqualWhenIdNull() {
        Item i1 = Item.builder().id(null).build();
        Item i2 = Item.builder().id(null).build();

        assertNotEquals(i1, i2);
    }

    @Test
    void shouldAddCommentAndSetBackReference() {
        Item item = new Item();
        Comment comment = new Comment();

        item.addComment(comment);

        assertEquals(1, item.getComments().size());
        assertEquals(item, comment.getItem());
    }
}