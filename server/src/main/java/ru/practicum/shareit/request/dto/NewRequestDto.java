package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewRequestDto {
    private String description;
}
