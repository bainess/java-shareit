package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateItemRequest {
    private Long id;
    private User owner;
    private String name;
    private String description;
    private boolean isAvailable;
    private LocalDate bookedFrom;
    private LocalDate bookedTo;

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasAvailability() {
        return isAvailable;
    }

    public boolean hasBookedFrom() {
        return ! (bookedFrom == null);
    }

    public boolean hasBookedTo() {
        return ! (bookedTo == null);
    }
}
