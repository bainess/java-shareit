package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void equals_shouldReturnTrueForSameObject() {
        User user = User.builder().id(1L).name("John").email("john@mail.com").build();
        assertEquals(user, user);
    }

    @Test
    void equals_shouldReturnTrueForSameId() {
        User user1 = User.builder().id(1L).name("John").email("john@mail.com").build();
        User user2 = User.builder().id(1L).name("Jane").email("jane@mail.com").build();
        assertEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentIds() {
        User user1 = User.builder().id(1L).name("John").email("john@mail.com").build();
        User user2 = User.builder().id(2L).name("John").email("john@mail.com").build();
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseWhenIdIsNull() {
        User user1 = User.builder().id(null).name("John").email("john@mail.com").build();
        User user2 = User.builder().id(1L).name("John").email("john@mail.com").build();
        assertNotEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        User user = User.builder().id(1L).build();
        assertNotEquals(user, "not a user");
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        User user = User.builder().id(1L).build();
        assertNotEquals(user, null);
    }

    @Test
    void hashCode_shouldBeSameForDifferentInstancesOfSameClass() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();

        assertEquals(user1.hashCode(), user2.hashCode());
        assertEquals(user1.hashCode(), User.class.hashCode());
    }

    @Test
    void builder_shouldCreateValidUser() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyUser() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void allArgsConstructor_shouldCreateUserWithAllFields() {
        User user = new User(1L, "John Doe", "john@example.com");

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void setters_shouldUpdateFields() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void getters_shouldReturnFields() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void toString_shouldNotBeNull() {
        User user = User.builder().id(1L).name("John").email("john@mail.com").build();
        assertNotNull(user.toString());
        assertTrue(user.toString().contains("John"));
    }

    @Test
    void equals_shouldHandleBothIdsNull() {
        User user1 = User.builder().id(null).name("John").build();
        User user2 = User.builder().id(null).name("Jane").build();
        assertNotEquals(user1, user2);
    }
}