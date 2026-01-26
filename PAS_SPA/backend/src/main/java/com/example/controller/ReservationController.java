package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;
import com.example.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ShowReservationDTO getReservationById(@PathVariable String id) {
        ShowReservationDTO dto = reservationService.findReservation(id)
                .orElseThrow(NotFoundException::new);

        return addHateoasLinks(dto);
    }

    @GetMapping
    public List<ShowReservationDTO> getAllReservations() {
        return reservationService.getAllReservations().stream()
                .map(this::addHateoasLinks)
                .toList();
    }

    @PutMapping("/{id}")
    public void updateReservation(@PathVariable String id,
                             @RequestBody @Valid CreateReservationDTO reservationDTO){
        reservationService.updateReservation(id, reservationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String id){
        reservationService.removeReservation(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> endReservation(@PathVariable String id){
        reservationService.endReservation(id);
        return ResponseEntity.ok().build();
    }

    private ShowReservationDTO addHateoasLinks(ShowReservationDTO dto) {
        String id = dto.getReservationId();

        dto.removeLinks();

        dto.add(linkTo(methodOn(ReservationController.class).getReservationById(id)).withSelfRel());
        dto.add(linkTo(methodOn(ReservationController.class).endReservation(id)).withRel("cancel"));
        dto.add(linkTo(methodOn(ReservationController.class).deleteReservation(id)).withRel("delete"));

        dto.add(linkTo(methodOn(UserController.class).getClientById(dto.getClientId())).withRel("user"));
        dto.add(linkTo(methodOn(RoomController.class).getRoomById(dto.getRoomId())).withRel("resource"));

        return dto;
    }

}
