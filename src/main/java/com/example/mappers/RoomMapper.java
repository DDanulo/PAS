package com.example.mappers;

import com.example.domain.Room;
import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public Room createRoomDTOToRoom(CreateRoomDTO roomDTO){
        return Room.builder()
                .roomType(roomDTO.getRoomType())
                .basePrice(roomDTO.getBasePrice())
                .capacity(roomDTO.getCapacity())
                .build();
    }

    public CreateRoomDTO roomToCreateRoomDTO(Room room){
        return CreateRoomDTO.builder()
                .roomType(room.getRoomType())
                .basePrice(room.getBasePrice())
                .capacity(room.getCapacity())
                .build();
    }

    public Room showRoomDTOToRoom(ShowRoomDTO roomDTO){
        return Room.builder()
                .roomId(roomDTO.getId())
                .roomType(roomDTO.getRoomType())
                .basePrice(roomDTO.getBasePrice())
                .capacity(roomDTO.getCapacity())
                .build();
    }

    public ShowRoomDTO roomToShowRoomDTO(Room room){
        return ShowRoomDTO.builder()
                .id(room.getRoomId())
                .roomType(room.getRoomType())
                .basePrice(room.getBasePrice())
                .capacity(room.getCapacity())
                .build();
    }
}
