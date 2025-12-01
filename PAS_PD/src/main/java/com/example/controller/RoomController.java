package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;
import com.example.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    public static final String BASE_URL = "/api/v1/rooms";
    public static final String BASE_ID_URL = "/api/v1/rooms/{id}";

    private final RoomService roomService;

    @PostMapping(BASE_URL)
    public ShowRoomDTO createRoom(@RequestBody @Valid CreateRoomDTO roomDTO){
        return roomService.addRoom(roomDTO);
    }

    @GetMapping(BASE_ID_URL)
    public ShowRoomDTO getRoomById(@PathVariable("id") String id){
        return (roomService.findRoom(id).orElseThrow(NotFoundException::new));
    }

    @GetMapping(BASE_URL)
    public List<ShowRoomDTO> getAllRooms(){
        return roomService.getAllRooms();
    }

    @PutMapping(BASE_ID_URL)
    public ShowRoomDTO updateRoom(@PathVariable String id,
                           @RequestBody @Valid CreateRoomDTO roomDTO){
        return roomService.updateRoom(id, roomDTO);
    }

    @DeleteMapping(BASE_ID_URL)
    public void deleteRoom(@PathVariable String id){
        roomService.removeRoom(id);
    }

}
