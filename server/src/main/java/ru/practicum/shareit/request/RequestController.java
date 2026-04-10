package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto saveRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody NewRequestDto request) {
        log.info("User id {} creates {}", userId, request);
        return requestService.saveRequest(userId, request);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable(name = "requestId") Long requestId) {
        log.info("User {} requires request {}", userId, requestId);
        return requestService.getRequest(userId, requestId);
    }
}
