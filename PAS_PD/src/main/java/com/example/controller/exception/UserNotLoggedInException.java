package com.example.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User is not authorised")
public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException() {
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }

    public UserNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLoggedInException(Throwable cause) {
        super(cause);
    }

    public UserNotLoggedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
