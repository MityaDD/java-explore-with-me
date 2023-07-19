package ru.practicum.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(NotFoundException e) {
        return ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                //.reason("Data not found exception")
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(DataIntegrityViolationException e) {
        return ApiError
                .builder()
                .status(HttpStatus.CONFLICT)
                //.timestamp(LocalDateTime.now())
                .reason("Validation exception")
                .message(e.getMessage())
                .build();
    }
}
