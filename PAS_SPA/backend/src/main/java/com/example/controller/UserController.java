package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.ChangePasswordDTO;
import com.example.model.users.*;
import com.example.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;

    @RolesAllowed("ADMIN")
    @GetMapping
    public List<ShowUserDTO> getAllClients() {
        return userService.getAllUsers();
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/client")
    public void createClient(@RequestBody @Valid CreateClientDTO userDTO) {
        userService.registerClient(userDTO);
    }

    @PostMapping("/admin")
    @RolesAllowed("ADMIN")
    public void createAdmin(@RequestBody @Valid CreateAdminDTO userDTO) {
        userService.registerAdmin(userDTO);
    }

    @PostMapping("/moderator")
    @RolesAllowed("ADMIN")
    public void createModerator(@RequestBody @Valid CreateModeratorDTO userDTO) {
        userService.registerModerator(userDTO);
    }

    @GetMapping("/id/{id}")
    @RolesAllowed("ADMIN")
    public ShowUserDTO getClientById(@PathVariable String id) {
        return userService.findUser(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public void updateClient(@PathVariable String id,
                             @RequestBody @Valid UpdateUserDTO userDTO) {
        userService.updateClient(id, userDTO);
    }

    @GetMapping("/by-login/{login}")
    @RolesAllowed("ADMIN")
    public ShowUserDTO getClientByLogin(@PathVariable String login) {
        return userService.getClientByLogin(login).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/search")
    @RolesAllowed("ADMIN")
    public List<ShowUserDTO> findClientsByLogin(@RequestParam String login) {
        return userService.findClientsByLogin(login);
    }

    @PostMapping("/{id}/activate")
    @RolesAllowed("ADMIN")
    public void activateClient(@PathVariable String id) {
        userService.activateClient(id);
    }

    @PostMapping("/{id}/deactivate")
    @RolesAllowed("ADMIN")
    public void deactivateClient(@PathVariable String id) {
        userService.deactivateClient(id);
    }

    @PatchMapping("/{id}/password")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> changePassword(@PathVariable String id,
                                            @RequestBody @Valid ChangePasswordDTO dto) {
        try {
            userService.changePassword(id, dto);
            return ResponseEntity.status(200).body("Zmieniono hasło");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Wystąpił nieoczekiwany błąd");
        }
    }
}