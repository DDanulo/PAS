package com.example.controller.exception;


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
