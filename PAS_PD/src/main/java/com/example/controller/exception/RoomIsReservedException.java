package com.example.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Room is already reserved at the moment.")
public class RoomIsReservedException extends RuntimeException {
    public RoomIsReservedException(String message) {
        super(message);
    }

    public RoomIsReservedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoomIsReservedException(Throwable cause) {
        super(cause);
    }

    public RoomIsReservedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RoomIsReservedException() {
    }
}
