package com.example.web_mvc.model.users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShowClientDTO extends ShowUserDTO {
    public ShowClientDTO(String id, String login, String firstName, String lastName, String email, Role role, Boolean isActive) {
        super(id, login, firstName, lastName, email, role, isActive);
    }

    public ShowClientDTO() {
    }
}
