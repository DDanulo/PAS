package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;
import com.example.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public void createReservation(@RequestBody @Valid CreateReservationDTO reservationDTO){
        reservationService.makeReservation(reservationDTO);
    }

    @GetMapping("/{id}")
    public ShowReservationDTO getReservationById(@PathVariable String id){
        return (reservationService.findReservation(id).orElseThrow(NotFoundException::new));
    }

    @GetMapping
    public List<ShowReservationDTO> getAllReservations(){
        return reservationService.getAllReservations();
    }

    @PutMapping("/{id}")
    public void updateReservation(@PathVariable String id,
                             @RequestBody @Valid CreateReservationDTO reservationDTO){
        reservationService.updateReservation(id, reservationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable String id){
        reservationService.removeReservation(id);
    }

    @GetMapping("/clients/{clientId}/reservations")
    public List<ShowReservationDTO> getClientReservation(
            @PathVariable String clientId,
            @RequestParam(required = false, defaultValue = "current") String status) {

        return switch (status) {
            case "current" -> reservationService.findCurrentForClient(clientId);
            case "past"    -> reservationService.findPastForClient(clientId);
            default        -> throw new IllegalArgumentException("status must be current|past");
        };
    }

    @GetMapping("/rooms/{roomId}/reservations")
    public List<ShowReservationDTO> getRoomReservation(
            @PathVariable String roomId,
            @RequestParam(required = false, defaultValue = "current") String status) {

        return switch (status) {
            case "current" -> reservationService.findCurrentForRoom(roomId);
            case "past"    -> reservationService.findPastForRoom(roomId);
            default        ->  reservationService.getAllReservations();
        };
    }

    @PostMapping("/{id}/end")
    public void endReservation(@PathVariable String id){
        reservationService.endReservation(id);
    }
}
