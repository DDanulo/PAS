package com.example.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Reservation ends before starting.")
public class ReservationEndsBeforeStarting extends RuntimeException {
    public ReservationEndsBeforeStarting() {
    }

    public ReservationEndsBeforeStarting(String message) {
        super(message);
    }

    public ReservationEndsBeforeStarting(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationEndsBeforeStarting(Throwable cause) {
        super(cause);
    }

    public ReservationEndsBeforeStarting(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
