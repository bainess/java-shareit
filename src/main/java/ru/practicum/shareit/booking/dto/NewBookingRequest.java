package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class NewBookingRequest {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
