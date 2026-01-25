package com.example.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.domain.User;
import com.example.model.LoginDTO;
import com.example.security.JwtService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO credentials) {

        User user = userService.findByLogin(credentials.getLogin());

        if (user == null) {
            return ResponseEntity.status(401).body("Brak użytkownika w systemie");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(
                credentials.getPassword().toCharArray(),
                user.getPassword().toCharArray()
        );

        if (!result.verified) {
            return ResponseEntity.status(401).body("Błędne hasło lub login"); // celowo jest login lub hasło, bo tak jak mówiliśmy na zajęciach żeby np ktoś nie wiedział z zewnątrz że trafił w login
        }

        if (!user.getIsActive()) {
            return ResponseEntity.status(403).body("Konto nie jest aktywne w systemie.");
        }

        String token = jwtService.generateToken(user.getLogin(), user.getRole().name());

        return ResponseEntity.ok(token);
    }
}