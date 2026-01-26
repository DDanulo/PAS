package com.example.controller;


import com.example.controller.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono wartości.");
    }
    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<String> handleAccountNotActiveException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Konto nie jest aktywne.");
    }
    @ExceptionHandler(ReservationHasEndedException.class)
    public ResponseEntity<String> handleReservationHasEndedException(ReservationHasEndedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Rezerwacja już jest zakończona.");
    }
    @ExceptionHandler(ReservationEndsBeforeStarting.class)
    public ResponseEntity<String> handleReservationHasEndedException(ReservationEndsBeforeStarting ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Rezerwacja się kończy przed jej początkiem.");
    }
    @ExceptionHandler(RoomIsReservedException.class)
    public ResponseEntity<String> handleRoomIsReservedException(RoomIsReservedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Pokój/sala jest obecnie zarezerwowana.");
    }
    @ExceptionHandler(SameLoginException.class)
    public ResponseEntity<String> handleSameLoginException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Użytkownik o takim samym loginie już istnieje.");
    }
    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<String> handleUserNotLoggedInException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Użytkownik nie jest uwierzytelniony.");
    }
}
