package com.example.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.domain.User;
import com.example.model.JwtTokenDTO;
import com.example.model.LoginDTO;
import com.example.security.JwtService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            return ResponseEntity.status(401).body("Błędne hasło lub login");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(
                credentials.getPassword().toCharArray(),
                user.getPassword().toCharArray()
        );

        if (!result.verified) {
            return ResponseEntity.status(401).body("Błędne hasło lub login");
        }

        if (!user.getIsActive()) {
            return ResponseEntity.status(403).body("Konto nie jest aktywne w systemie.");
        }

        String access = jwtService.generateToken(user.getLogin(), user.getRole().name());
        String refresh = jwtService.generateRefreshToken(user.getLogin(), user.getRole().name());

        return ResponseEntity.ok(new JwtTokenDTO(access, refresh));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken != null && jwtService.validateToken(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            String role = jwtService.extractRole(refreshToken);

            User user = userService.findByLogin(username);
            if (user == null || !user.getIsActive()) {
                return ResponseEntity.status(401).body("Użytkownik nieaktywny");
            }

            String newAccess = jwtService.generateToken(username, role);
            String newRefresh = jwtService.generateRefreshToken(username, role);

            return ResponseEntity.ok(new JwtTokenDTO(newAccess, newRefresh));
        }

        return ResponseEntity.status(401).body("Nieprawidłowy refreshToken");
    }
}