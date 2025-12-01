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
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomServiceMongo implements RoomService {
    private final RoomMapper roomMapper;
    private final RoomRepository repository;
    private final MongoClient mongoClient;

    @Override
    public ShowRoomDTO addRoom(CreateRoomDTO room) {

        if (room == null) {
            throw new IllegalArgumentException();
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                Room room1 = repository.add(session, roomMapper.createRoomDTOToRoom(room));
                session.commitTransaction();
                return roomMapper.roomToShowRoomDTO(room1);
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot add room", e);
            }
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
        ObjectId objectId = new ObjectId(id);
        if (id == null) {
            throw new IllegalArgumentException("Wrong room id");
        }

        if (findRoom(id) == null) {
            throw new IllegalArgumentException("Room not found");
        }
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                repository.remove(session, objectId);
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw new RuntimeException("Cannot remove room", e);
            }
        }
    }

    @Override
    public ShowRoomDTO updateRoom(String id, CreateRoomDTO room) {
        ObjectId objectId = new ObjectId(id);
        if (findRoom(id).isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        Room toUpdate = roomMapper.createRoomDTOToRoom(room);
        toUpdate.setRoomId(objectId);
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            Room updated;
            try {
                updated = repository.update(session, objectId, toUpdate);
                session.commitTransaction();
            } catch (MongoException ex) {
                if (session.hasActiveTransaction()) {
                    session.abortTransaction();
                }
                throw new RuntimeException("Cannot update room", ex);
            }
            return roomMapper.roomToShowRoomDTO(updated);
        }
    }
}
