package com.example.model.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateClientDTO {

    @NotNull
    @Size(min = 5, max = 20)
    private String login;

    @NotNull
    @Size(min = 3, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 30)
    private String lastName;

    @NotNull
    @Email
    @Size(min = 3, max = 50)
    private String email;

    @NotNull
    private Boolean isActive;
}
