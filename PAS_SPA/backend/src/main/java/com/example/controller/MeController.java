package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.ChangePasswordDTO;
import com.example.model.ShowReservationDTO;
import com.example.model.users.ShowUserDTO;
import com.example.model.users.UpdateUserDTO;
import com.example.service.ReservationService;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MeController {

    private final UserService userService;
    private final ReservationService reservationService;

    @GetMapping()
    public ShowUserDTO getMe(@AuthenticationPrincipal String username) {
        return userService.getClientByLogin(username).orElseThrow(NotFoundException::new);
    }

    @PutMapping()
    public void updateClient(@AuthenticationPrincipal String username,
                             @RequestBody @Valid UpdateUserDTO userDTO) {
        userService.getClientByLogin(username).ifPresentOrElse(
                (u) -> userService.updateClient(u.getId(), userDTO),
                NotFoundException::new
        );
    }

    @GetMapping("/reservations")
    public List<ShowReservationDTO> getMyAllReservations(@AuthenticationPrincipal String username) {
        AtomicReference<ShowUserDTO> user = new AtomicReference<>();
        userService.getClientByLogin(username).ifPresentOrElse(
                user::set,
                NotFoundException::new
        );
        return reservationService.findAllForClient(user.get().getId()).stream().map(this::addHateoasLinks).toList();
    }

    @GetMapping("/reservations/ended")
    public List<ShowReservationDTO> getMyEndedReservations(@AuthenticationPrincipal String username) {
        AtomicReference<ShowUserDTO> user = new AtomicReference<>();
        userService.getClientByLogin(username).ifPresentOrElse(
                user::set,
                NotFoundException::new
        );
        return reservationService.findPastForClient(user.get().getId()).stream().map(this::addHateoasLinks).toList();
    }

    @GetMapping("/reservations/active")
    public List<ShowReservationDTO> getMyActiveReservations(@AuthenticationPrincipal String username) {
        AtomicReference<ShowUserDTO> user = new AtomicReference<>();
        userService.getClientByLogin(username).ifPresentOrElse(
                user::set,
                NotFoundException::new
        );
        return reservationService.findCurrentForClient(user.get().getId()).stream().map(this::addHateoasLinks).toList();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changeMyPassword(@AuthenticationPrincipal String username,
                                              @RequestBody @Valid ChangePasswordDTO dto) {
        try {
            userService.getClientByLogin(username).ifPresentOrElse(
                    (u) -> userService.changePassword(u.getId(), dto),
                    NotFoundException::new
            );
            return ResponseEntity.status(200).body("Zmieniono hasło");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił nieoczekiwany błąd");
        }
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
