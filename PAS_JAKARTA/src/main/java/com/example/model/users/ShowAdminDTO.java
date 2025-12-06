package com.example.model.users;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShowAdminDTO extends ShowUserDTO {

    public ShowAdminDTO(String id, String login, String firstName, String lastName, String email, Role role, Boolean isActive) {
        super(id, login, firstName, lastName, email, role, isActive);
    }

    public ShowAdminDTO() {
    }
}
