package com.example.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ShowUserDTO {

    @NotNull
    private UUID id;

    @NotNull
    private String login;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;
}
