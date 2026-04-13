package ru.practicum.shareit.item.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long itemId;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
