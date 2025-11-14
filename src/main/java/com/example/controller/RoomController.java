package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;
import com.example.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public void createRoom(@RequestBody @Valid CreateRoomDTO roomDTO){
        roomService.addRoom(roomDTO);
    }

    @GetMapping("/{id}")
    public ShowRoomDTO getRoomById(@PathVariable UUID id){
        return (roomService.findRoom(id).orElseThrow(NotFoundException::new));
    }

    @GetMapping
    public List<ShowRoomDTO> getAllRooms(){
        return roomService.getAllRooms();
    }

    @PutMapping("/{id}")
    public void updateRoom(@PathVariable UUID id,
                           @RequestBody @Valid CreateRoomDTO roomDTO){
        roomService.updateRoom(roomDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable UUID id){
        roomService.removeRoom(id);
    }

}
