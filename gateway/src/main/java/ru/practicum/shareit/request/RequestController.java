package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> saveRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody RequestDto request) {
        log.info("User {} request {}", userId, request);
        return requestClient.saveRequest(userId, request);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestByIdandUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @PathVariable(name = "requestId") Long requestId) {
        log.info("User {} requests request {}", userId, requestId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("User {} requests their requests");
        return requestClient.getRequestsByUser(userId);
    }
}
