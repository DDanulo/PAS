package com.example.model;

import com.example.mappers.UserTypes;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateUserDTO {

    @NotNull
    private String login;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private UserTypes userTypes;

    private Boolean isTrainerPartner;
}
