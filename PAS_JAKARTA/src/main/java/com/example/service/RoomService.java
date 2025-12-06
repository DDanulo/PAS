package com.example.service;

import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    ShowRoomDTO addRoom(CreateRoomDTO room);

    Optional<ShowRoomDTO> findRoom(String id);

    List<ShowRoomDTO> getAllRooms();

    void removeRoom(String id);

    ShowRoomDTO updateRoom(String id, CreateRoomDTO room);
}
