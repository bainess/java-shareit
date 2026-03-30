package ru.practicum.shareit.item.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long itemId;
    private String text;
    private Long authorId;
    private LocalDateTime created;
}
