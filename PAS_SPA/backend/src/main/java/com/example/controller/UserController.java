package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.ChangePasswordDTO;
import com.example.model.users.*;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<ShowUserDTO> getAllClients() {
        return userService.getAllUsers();
    }

    @PostMapping("/client")
    public void createClient(@RequestBody @Valid CreateClientDTO userDTO) {
        userService.registerClient(userDTO);
    }

    @PostMapping("/admin")
    public void createAdmin(@RequestBody @Valid CreateAdminDTO userDTO) {
        userService.registerAdmin(userDTO);
    }

    @PostMapping("/moderator")
    public void createModerator(@RequestBody @Valid CreateModeratorDTO userDTO) {
        userService.registerModerator(userDTO);
    }

    @GetMapping("/{id}")
    public ShowUserDTO getClientById(@PathVariable String id) {
        return (userService.findUser(id).orElseThrow(NotFoundException::new));
    }

    @PutMapping("/{id}")
    public void updateClient(@PathVariable String id,
                             @RequestBody @Valid UpdateUserDTO userDTO) {
        userService.updateClient(id, userDTO);
    }

    @GetMapping("/by-login/{login}")
    public ShowUserDTO getClientByLogin(@PathVariable String login) {
        return userService.getClientByLogin(login).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/search")
    public List<ShowUserDTO> findClientsByLogin(@RequestParam String login) {
        return userService.findClientsByLogin(login);
    }

    @PostMapping("/{id}/activate")
    public void activateClient(@PathVariable String id) {
        userService.activateClient(id);
    }

    @PostMapping("/{id}/deactivate")
    public void deactivateClient(@PathVariable String id) {
        userService.deactivateClient(id);
    }

    @PatchMapping("/{id}/password")
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