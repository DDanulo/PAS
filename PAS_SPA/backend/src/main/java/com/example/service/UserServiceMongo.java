package com.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.controller.exception.SameLoginException;
import com.example.domain.Admin;
import com.example.domain.Client;
import com.example.domain.Moderator;
import com.example.domain.User;
import com.example.mappers.UserMapper;
import com.example.model.ChangePasswordDTO;
import com.example.model.users.*;
import com.example.repository.UserRepository;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceMongo implements UserService {

    private final UserRepository repository;
    private final MongoClient mongoClient;
    private final UserMapper userMapper;

    @Override
    public void registerClient(CreateClientDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");
        if (repository.findByLogin(dto.getLogin()).isPresent()) {
            throw new SameLoginException("Login zajęty");
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                Client client = userMapper.toClient(dto);
                String hashedPassword = BCrypt.withDefaults().hashToString(12, dto.getPassword().toCharArray());
                client.setPassword(hashedPassword);
                repository.add(session, client);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add Client", e);
            }
        }
    }

    @Override
    public void registerAdmin(CreateAdminDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                Admin admin = userMapper.toAdmin(dto);
                String hashedPassword = BCrypt.withDefaults().hashToString(12, dto.getPassword().toCharArray());
                admin.setPassword(hashedPassword);
                repository.add(session, admin);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add Admin", e);
            }
        }
    }

    @Override
    public void registerModerator(CreateModeratorDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                Moderator moderator = userMapper.toModerator(dto);
                String hashedPassword = BCrypt.withDefaults().hashToString(12, dto.getPassword().toCharArray());
                moderator.setPassword(hashedPassword);
                repository.add(session, moderator);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add Moderator", e);
            }
        }
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login).orElse(null);
    }

    @Override
    public Optional<ShowUserDTO> findUser(String id) {
        return repository.findById(new ObjectId(id)).map(userMapper::UserToDto);
    }

    @Override
    public List<ShowUserDTO> getAllUsers() {
        return repository.findAll().stream().map(userMapper::UserToDto).toList();
    }

    @Override
    public void updateClient(String id, UpdateUserDTO dto) {
        org.bson.types.ObjectId objectId = new org.bson.types.ObjectId(id);

        com.example.domain.User user = repository.findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        try (com.mongodb.client.ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                userMapper.updateUserFromDto(dto, user);

                repository.update(session, objectId, user);

                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot update Client", e);
            }
        }
    }

    @Override
    public Optional<ShowUserDTO> getClientByLogin(String login) {
        return repository.findByLogin(login).map(userMapper::UserToDto);
    }

    @Override
    public List<ShowUserDTO> findClientsByLogin(String login) {
        return repository.searchByLogin(login).stream().map(userMapper::UserToDto).toList();
    }

    @Override
    public void activateClient(String id) {
        ObjectId objectId = new ObjectId(id);
        repository.activateAccount(null, objectId);
    }

    @Override
    public void deactivateClient(String id) {
        ObjectId objectId = new ObjectId(id);
        repository.deactivateAccount(null, objectId);
    }

    @Override
    public void changePassword(String id, ChangePasswordDTO dto) {
        ObjectId objectId = new ObjectId(id);
        User user = repository.findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        BCrypt.Result result = BCrypt.verifyer().verify(dto.getOldPassword().toCharArray(), user.getPassword());
        if (!result.verified) {
            throw new IllegalArgumentException("Wprowadzone obecne hasło jest niepoprawne");
        }

        String hashedNewPassword = BCrypt.withDefaults().hashToString(12, dto.getNewPassword().toCharArray());

        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                user.setPassword(hashedNewPassword);
                repository.update(session, objectId, user);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Nie można zmienić hasła", e);
            }
        }
    }
}