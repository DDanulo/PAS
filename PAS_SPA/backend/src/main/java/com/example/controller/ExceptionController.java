package com.example.controller;


import com.example.controller.exception.AccountNotActiveException;
import com.example.controller.exception.NotFoundException;
import com.example.controller.exception.ReservationHasEndedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity handleAccountNotActiveException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @ExceptionHandler(ReservationHasEndedException.class)
    public ResponseEntity handleReservationHasEndedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
