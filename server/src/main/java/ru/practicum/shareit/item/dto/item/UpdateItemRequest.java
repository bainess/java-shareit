package ru.practicum.shareit.item.dto.item;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateItemRequest {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private LocalDateTime bookedFrom;
    private LocalDateTime bookedTo;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasAvailability() {
        return !(available == null);
    }

    public boolean hasBookedFrom() {
        return !(bookedFrom == null);
    }

    public boolean hasBookedTo() {
        return !(bookedTo == null);
    }
}
