package ru.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private String reason;
    private String message;

    public ApplicationException(String reason, String message) {
        super();
        this.reason = reason;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}