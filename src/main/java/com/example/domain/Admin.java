package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Admin extends User {
    public Admin(UUID userId, String login, String firstName, String lastName, String email, Boolean isActive) {
        super(userId, login, firstName, lastName, email, isActive);
    }
}
