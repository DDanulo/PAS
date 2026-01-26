package com.example.service;

import com.example.domain.User;
import com.example.model.ChangePasswordDTO;
import com.example.model.users.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerClient(CreateClientDTO user);

    void registerAdmin(CreateAdminDTO user);

    void registerModerator(CreateModeratorDTO user);

    Optional<ShowUserDTO> findUser(String id);

    List<ShowUserDTO> getAllUsers();

    void updateClient(String id, UpdateUserDTO dto);

    Optional<ShowUserDTO> getClientByLogin(String login);

    List<ShowUserDTO> findClientsByLogin(String login);

    void activateClient(String id);

    void deactivateClient(String id);

    User findByLogin(String login);

    void changePassword(String id, ChangePasswordDTO dto);
}