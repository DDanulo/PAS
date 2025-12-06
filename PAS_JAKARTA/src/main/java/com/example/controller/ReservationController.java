package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;
import com.example.service.ReservationService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/v1/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationController {
    private ReservationService reservationService;

    @Inject
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @POST
    public void createReservation(@Valid CreateReservationDTO reservationDTO){
        reservationService.makeReservation(reservationDTO);
    }

    @GET
    @Path("/{id}")
    public ShowReservationDTO getReservationById(@PathParam("id") String id){
        return (reservationService.findReservation(id).orElseThrow(NotFoundException::new));
    }

    @GET
    public List<ShowReservationDTO> getAllReservations(){
        return reservationService.getAllReservations();
    }

    @PUT
    @Path("/{id}")
    public void updateReservation(@PathParam("id") String id,
                             @Valid CreateReservationDTO reservationDTO){
        reservationService.updateReservation(id, reservationDTO);
    }

    @DELETE
    @Path("/{id}")
public void deleteReservation(@PathParam("id") String id){
        reservationService.removeReservation(id);
    }

    @GET
    @Path("/clients/{clientId}/reservations")
    public List<ShowReservationDTO> getClientReservation(
            @PathParam("clientId") String clientId,
            @QueryParam("status") @DefaultValue("current") String status) {

        return switch (status) {
            case "current" -> reservationService.findCurrentForClient(clientId);
            case "past"    -> reservationService.findPastForClient(clientId);
            default        -> throw new IllegalArgumentException("status must be current|past");
        };
    }


    @GET
    @Path("/rooms/{roomId}/reservations")
    public List<ShowReservationDTO> getRoomReservation(
            @PathParam("roomId") String roomId,
            @QueryParam("status") @DefaultValue("current") String status) {

        return switch (status) {
            case "current" -> reservationService.findCurrentForRoom(roomId);
            case "past"    -> reservationService.findPastForRoom(roomId);
            default        ->  reservationService.getAllReservations();
        };
    }

    @POST
    @Path("/{id}/end")
    public void endReservation(@PathParam("id") String id){
        reservationService.endReservation(id);
    }
}
