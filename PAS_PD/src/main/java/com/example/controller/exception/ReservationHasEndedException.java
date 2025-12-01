package com.example.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Reservation has ended.")
public class ReservationHasEndedException extends RuntimeException {
    public ReservationHasEndedException() {
    }

    public ReservationHasEndedException(String message) {
        super(message);
    }

    public ReservationHasEndedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationHasEndedException(Throwable cause) {
        super(cause);
    }

    public ReservationHasEndedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
