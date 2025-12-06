package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.CreateRoomDTO;
import com.example.model.ShowRoomDTO;
import com.example.service.RoomService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {

    public static final String BASE_URL = "/api/v1/rooms";
    public static final String BASE_ID_URL = "/api/v1/rooms/{id}";

    private RoomService roomService;

    @Inject
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @POST
    @Path(BASE_URL)
    public ShowRoomDTO createRoom(@Valid CreateRoomDTO roomDTO) {
        return roomService.addRoom(roomDTO);
    }

    @GET
    @Path(BASE_ID_URL)
    public ShowRoomDTO getRoomById(@PathParam("id") String id) {
        return (roomService.findRoom(id).orElseThrow(NotFoundException::new));
    }

    @GET
    @Path(BASE_URL)
    public List<ShowRoomDTO> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PUT
    @Path(BASE_ID_URL)
    public ShowRoomDTO updateRoom(@PathParam("id") String id,
                                  @Valid CreateRoomDTO roomDTO) {
        return roomService.updateRoom(id, roomDTO);
    }

    @DELETE
    @Path(BASE_ID_URL)
    public void deleteRoom(@PathParam("id") String id) {
        roomService.removeRoom(id);
    }

}
