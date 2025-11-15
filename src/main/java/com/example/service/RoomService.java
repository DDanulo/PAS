package com.example.service;

import com.example.mappers.RoomMapper;
import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;
import com.example.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository repository;
    private final EntityManager em;
    private final EntityTransaction transaction;
    private final RoomMapper roomMapper;

    public void addRoom(CreateRoomDTO obj) {
        transaction.begin();
        repository.add(roomMapper.createRoomDTOToRoom(obj), em);
        transaction.commit();
    }

    public void removeRoom(UUID id) {
        transaction.begin(); //TODO:check if room is not currently used or planned to be used in the future.
        repository.removeById(id, em);
        transaction.commit();
    }
/*
    public Optional<ShowRoomDTO> findRoom(UUID id) {
        return Optional.ofNullable(roomMapper.roomToShowRoomDTO(repository.findById(id, em).orElse(null)));
    } */

    public List<ShowRoomDTO> getAllRooms() {
        return repository.findAll(em).stream().map(roomMapper::roomToShowRoomDTO).toList();
    }

    public void updateRoom(CreateRoomDTO room, UUID id) {
        transaction.begin();
        repository.updateElement(roomMapper.createRoomDTOToRoom(room), id, em);
        transaction.commit();
    }
}
