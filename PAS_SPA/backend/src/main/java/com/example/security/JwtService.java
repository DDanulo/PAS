package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expTime}")
    private long expirationTime;

    public String generateToken(String username, String role) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("role", role);
        return createToken(credentials, username);
    }

    private String createToken(Map<String, Object> credentials, String subject) {

        return Jwts.builder()
                .setClaims(credentials)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (expirationTime * 1000)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractCredential(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractCredential(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractCredential(String token, Function<Claims, T> credentialsResolver) {
        final Claims claims = extractAllCredentials(token);
        return credentialsResolver.apply(claims);
    }

    private Claims extractAllCredentials(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}