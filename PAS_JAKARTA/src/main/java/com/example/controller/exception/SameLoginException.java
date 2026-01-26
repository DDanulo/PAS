package com.example.controller.exception;


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
