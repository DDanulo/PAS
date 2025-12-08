package com.example.controller.exception;

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
