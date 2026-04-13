package ru.practicum.shareit.item.dto.item;

import lombok.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDtoWithBookingDatesAndComments {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private LocalDateTime nextBooking;
    private LocalDateTime lastBooking;
    private List<CommentDto> comments;
}
