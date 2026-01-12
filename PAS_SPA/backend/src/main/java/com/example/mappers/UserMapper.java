package com.example.mappers;

import com.example.domain.*;
import com.example.model.users.Role;
import com.example.model.users.*;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public ShowUserDTO UserToDto(User user) {
        if (user == null) return null;

        ShowUserDTO base = ShowUserDTO.builder()
                .id(user.getUserId().toString())
                .login(user.getLogin())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();

        switch (user) {
            case Client client -> {
                ShowClientDTO dto = new ShowClientDTO();
                copyBase(base, dto);
                return dto;
            }
            case Moderator moderator -> {
                ShowModeratorDTO dto = new ShowModeratorDTO();
                copyBase(base, dto);
                return dto;
            }
            case Admin admin -> {
                ShowAdminDTO dto = new ShowAdminDTO();
                copyBase(base, dto);
                return dto;
            }
            default -> {
                return base;
            }
        }
    }

    private void copyBase(ShowUserDTO base, ShowUserDTO target) {
        target.setLogin(base.getLogin());
        target.setId(base.getId());
        target.setEmail(base.getEmail());
        target.setFirstName(base.getFirstName());
        target.setLastName(base.getLastName());
        target.setIsActive(base.getIsActive());
        target.setRole(base.getRole());
    }


    public Client toClient(CreateClientDTO dto) {
        Client client = new Client();
        client.setLogin(dto.getLogin());
        client.setEmail(dto.getEmail());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setRole(Role.CLIENT);
        client.setIsActive(dto.getIsActive());
        return client;
    }

    public Moderator toModerator(CreateModeratorDTO dto) {
        Moderator moderator = new Moderator();
        moderator.setLogin(dto.getLogin());
        moderator.setEmail(dto.getEmail());
        moderator.setFirstName(dto.getFirstName());
        moderator.setLastName(dto.getLastName());
        moderator.setRole(Role.MODERATOR);
        moderator.setIsActive(dto.getIsActive());
        return moderator;
    }

    public Admin toAdmin(CreateAdminDTO dto) {
        Admin admin = new Admin();
        admin.setLogin(dto.getLogin());
        admin.setEmail(dto.getEmail());
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setRole(Role.ADMIN);
        admin.setIsActive(dto.getIsActive());
        return admin;
    }
}
