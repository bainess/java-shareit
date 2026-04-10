package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Request;

public class RequestMapper {
    public static RequestDto mapToDto(Request request) {
        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static Request mapToRequest(NewRequestDto dto) {
        Request request = new Request();
        request.setDescription(dto.getDescription());
        return request;
    }
}
