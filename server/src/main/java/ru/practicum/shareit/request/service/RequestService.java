package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public RequestDto saveRequest(Long userId, NewRequestDto dto) {
        Request request = RequestMapper.mapToRequest(dto);
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User id " + userId + "was not found"));
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        request = requestRepository.save(request);
        return RequestMapper.mapToDto(request);
    }

    public RequestDto getRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request " + requestId + " by user " + userId + " was not found"));
        List<ItemDto> items = itemRepository.findAllByRequest_Id(requestId).stream().map(ItemMapper::itemToDto).toList();

        return RequestMapper.mapToDtoWithItemList(request,items);
    }

    public List<RequestDto> getAllRequestsByUser(Long userId) {
        List<Request> requests = requestRepository.findAllByRequestor_Id(userId);
        return requests.stream().map(RequestMapper::mapToDto).toList();
    }

}
