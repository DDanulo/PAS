package com.example.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User with a same login exists.")
public class SameLoginException extends RuntimeException {
    public SameLoginException(String message) {
        super(message);
    }

    public SameLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameLoginException(Throwable cause) {
        super(cause);
    }

    public SameLoginException() {
    }
}
