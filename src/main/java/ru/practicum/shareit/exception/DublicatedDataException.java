package ru.practicum.shareit.exception;

public class DublicatedDataException extends RuntimeException {
    public DublicatedDataException(String message) {
        super(message);
    }
}
