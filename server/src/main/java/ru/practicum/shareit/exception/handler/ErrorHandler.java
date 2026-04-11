package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NotFoundException e) {
        log.error("Not found error: {}", e.getMessage());
        return Map.of(
                "error", "not found",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleConflict(DuplicatedDataException e) {
        log.error("Conflict error: {}", e.getMessage());
        return Map.of(
                "error", "insufficient data",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleForbidden(ForbiddenException e) {
        log.error("Forbidden error: {}", e.getMessage());
        return Map.of(
                "error", "access forbidden",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleBadRequest(IllegalAccessException e) {
        log.error("Bad request error: {}", e.getMessage());
        return Map.of(
                "error", "illegal argument",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(RuntimeException e) {
        log.error("Internal error: {}", e.getMessage(), e);
        return Map.of(
                "error", "internal error",
                "message", e.getMessage()
        );
    }
}