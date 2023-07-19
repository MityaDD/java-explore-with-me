package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({MethodArgumentNotValidException.class,
            NotValidException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<HttpStatus, String> handleException(Exception e) {
        log.error("Ошибка 400: {}", e.getMessage());
        return Map.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<HttpStatus, String> handleThrowable(final Throwable e) {
        log.error("Ошибка: {}", e.getClass());
        return Map.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}

