package com.example.web_mvc.model.users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShowModeratorDTO extends ShowUserDTO {
    public ShowModeratorDTO() {
    }

    public ShowModeratorDTO(String id, String login, String firstName, String lastName, String email, Role role, Boolean isActive) {
        super(id, login, firstName, lastName, email, role, isActive);
    }
}
