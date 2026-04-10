package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dal.RequestRepository;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestDto saveRequest(Long userId, NewRequestDto dto) {
        Request request = RequestMapper.mapToRequest(dto);
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User id " + userId + "was not found"));
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        log.debug("Resource is to save {}", request);
        request = requestRepository.save(request);
        log.debug("Resource was saved {}", request);
        return RequestMapper.mapToDto(request);
    }

    public RequestDto getRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request " + requestId + " by user " + userId + " was not found"));
        return RequestMapper.mapToDto(request);
    }

}
