package com.example.service;

import com.example.controller.exception.NotFoundException;
import com.example.controller.exception.SameLoginException;
import com.example.mappers.UserMapper;
import com.example.model.users.CreateAdminDTO;
import com.example.model.users.CreateClientDTO;
import com.example.model.users.CreateModeratorDTO;
import com.example.model.users.ShowUserDTO;
import com.example.repository.UserRepository;
import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserServiceMongo implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final Provider<MongoClient> mongoClientProvider;

    @Inject
    public UserServiceMongo(UserRepository repository,
                            Provider<MongoClient> mongoClientProvider,
                            UserMapper userMapper) {
        this.repository = repository;
        this.mongoClientProvider = mongoClientProvider;
        this.userMapper = userMapper;
    }

    @Override
    public void registerClient(CreateClientDTO user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.startTransaction();
            try {
                repository.add(session, userMapper.toClient(user));
                session.commitTransaction();
            } catch (MongoWriteException e) {
                session.abortTransaction();
                throw new SameLoginException("Cannot add Client", e);
            }
        }
    }

    @Override
    public void registerAdmin(CreateAdminDTO user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.startTransaction();
            try {
                repository.add(session, userMapper.toAdmin(user));
                session.commitTransaction();
            } catch (MongoWriteException e) {
                session.abortTransaction();
                throw new SameLoginException("Cannot add Client", e);
            }
        }
    }

    @Override
    public void registerModerator(CreateModeratorDTO user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.startTransaction();
            try {
                repository.add(session, userMapper.toModerator(user));
                session.commitTransaction();
            } catch (MongoWriteException e) {
                session.abortTransaction();
                throw new SameLoginException("Cannot add Client", e);
            }
        }
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
    public void updateClient(String id, CreateClientDTO Client) {
        ObjectId objectId = new ObjectId(id);
        if (findUser(id).isEmpty()) {
            throw new NotFoundException("Client not found");
        }
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.startTransaction();
            try {
                repository.update(session, objectId, userMapper.toClient(Client));
                session.commitTransaction();
            }  catch (MongoWriteException e) {
                session.abortTransaction();
                throw new SameLoginException("Cannot add Client", e);
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
        repository.setActiveStatus(null, objectId);
    }

    @Override
    public void deactivateClient(String id) {
        ObjectId objectId = new ObjectId(id);
        repository.setInactiveStatus(null, objectId);
    }
}