package com.example.service;

import com.example.controller.exception.NotFoundException;
import com.example.domain.Room;
import com.example.mappers.RoomMapper;
import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;
import com.example.repository.RoomRepository;
import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RoomServiceMongo implements RoomService {
    private final RoomMapper roomMapper;
    private final RoomRepository repository;
    private final Provider<MongoClient> mongoClientProvider;

    @Inject
    public RoomServiceMongo(RoomMapper roomMapper,
                            RoomRepository repository,
                            Provider<MongoClient> mongoClientProvider) {
        this.roomMapper = roomMapper;
        this.repository = repository;
        this.mongoClientProvider = mongoClientProvider;
    }

    @Override
    public ShowRoomDTO addRoom(CreateRoomDTO room) {
        if (room == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            return session.withTransaction(() -> {
                Room room1 = repository.add(session, roomMapper.createRoomDTOToRoom(room));
                return roomMapper.roomToShowRoomDTO(room1);
            });
        }
    }

    @Override
    public Optional<ShowRoomDTO> findRoom(String id) {
        return repository.findById(new ObjectId(id)).map(roomMapper::roomToShowRoomDTO);
    }

    @Override
    public List<ShowRoomDTO> getAllRooms() {
        return repository.findAll().stream().map(roomMapper::roomToShowRoomDTO).toList();
    }

    @Override
    public void removeRoom(String id) {
        if (id == null) {
            throw new NotFoundException("Wrong room id");
        }
        if (findRoom(id).isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        ObjectId objectId = new ObjectId(id);

        try (ClientSession session = mongoClientProvider.get().startSession()) {
            session.withTransaction(() -> {
                repository.remove(session, objectId);
                return null;
            });
        }
    }

    @Override
    public ShowRoomDTO updateRoom(String id, CreateRoomDTO room) {
        if (findRoom(id).isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        ObjectId objectId = new ObjectId(id);
        Room toUpdate = roomMapper.createRoomDTOToRoom(room);
        toUpdate.setRoomId(objectId);
        try (ClientSession session = mongoClientProvider.get().startSession()) {
            return session.withTransaction(() -> {
                Room room1 = repository.update(session, objectId, toUpdate);
                return roomMapper.roomToShowRoomDTO(room1);
            });
        }
    }
}