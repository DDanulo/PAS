package com.example.service;

import com.example.mappers.UserMapper;
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
    public void registerClient(CreateClientDTO user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.add(session, userMapper.toClient(user));
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add Client", e);
            }
        }
    }
    @Override
    public void registerAdmin(CreateAdminDTO user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.add(session, userMapper.toAdmin(user));
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add Admin", e);
            }
        }
    }

    @Override
    public void registerModerator(CreateModeratorDTO user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.add(session, userMapper.toModerator(user));
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add Moderator", e);
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

//    public void removeClient(ObjectId id) {
//        if (id == null) {
//            throw new IllegalArgumentException("Wrong Client id");
//        }
//
//        if (findClient(id).isEmpty()) {
//            throw new IllegalArgumentException("Client not found");
//        }
//        try (ClientSession session = mongoClient.startSession()) {
//            session.startTransaction();
//            try {
//                repository.remove(session, id);
//                session.commitTransaction();
//            } catch (Exception e) {
//                session.abortTransaction();
//                throw new RuntimeException("Cannot remove Client", e);
//            }
//        }
//    }

    @Override
    public void updateClient(String id, CreateClientDTO Client) {
        ObjectId objectId = new ObjectId(id);
        if (objectId == null) {
            throw new IllegalArgumentException("Wrong Client id");
        }
        if (findUser(id).isEmpty()) {
            throw new IllegalArgumentException("Client not found");
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.update(session, objectId, userMapper.toClient(Client));
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
        repository.setActiveStatus(null, objectId);
    }

    @Override
    public void deactivateClient(String id) {
        ObjectId objectId = new ObjectId(id);
        repository.setInactiveStatus(null, objectId);
    }


}
