package com.example.service;

import com.example.model.users.CreateAdminDTO;
import com.example.model.users.CreateClientDTO;
import com.example.model.users.CreateModeratorDTO;
import com.example.model.users.ShowUserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerClient(CreateClientDTO user);

    void registerAdmin(CreateAdminDTO user);

    void registerModerator(CreateModeratorDTO user);

    Optional<ShowUserDTO> findUser(String id);

    List<ShowUserDTO> getAllUsers();

    void updateClient(String id, CreateClientDTO Client);

    Optional<ShowUserDTO> getClientByLogin(String login);

    List<ShowUserDTO> findClientsByLogin(String login);

    void activateClient(String id);

    void deactivateClient(String id);
}
