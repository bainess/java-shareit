package ru.practicum.shareit.item.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NewCommentRequest {
    private String text;
}
