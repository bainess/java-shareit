package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public class RequestMapper {
    public static RequestDto mapToDto(Request request) {
        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public static RequestDto mapToDtoWithItemList(Request request, List<ItemDto> items) {
        RequestDto dto = new RequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(items);
        return dto;
    }

    public static RequestDto mapToRequestNoItems(Request request) {
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
