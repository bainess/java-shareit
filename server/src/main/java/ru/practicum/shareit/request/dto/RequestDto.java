package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}
