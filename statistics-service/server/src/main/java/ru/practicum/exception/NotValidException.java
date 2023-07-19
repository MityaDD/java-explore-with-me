package ru.practicum.exception;

public class NotValidException extends RuntimeException {
    public NotValidException(final String message) {
        super(message);
    }
}
