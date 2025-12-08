package com.example.controller.exception;

public class ReservationIsAlreadyEndedException extends RuntimeException {
    public ReservationIsAlreadyEndedException() {
    }

    public ReservationIsAlreadyEndedException(String message) {
        super(message);
    }

    public ReservationIsAlreadyEndedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationIsAlreadyEndedException(Throwable cause) {
        super(cause);
    }
}
