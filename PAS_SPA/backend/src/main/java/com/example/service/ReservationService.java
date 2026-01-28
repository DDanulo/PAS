package com.example.service;

import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    void makeReservation(CreateReservationDTO reservation);

    List<ShowReservationDTO> getAllReservations();

    Optional<ShowReservationDTO> findReservation(String id);

    void removeReservation(String id);

    void updateReservation(String id, CreateReservationDTO res);

    List<ShowReservationDTO> findCurrentForClient(String clientId);

    List<ShowReservationDTO> findPastForClient(String clientId);
    List<ShowReservationDTO> findAllForClient(String clientId);

    List<ShowReservationDTO> findCurrentForRoom(String roomId);

    List<ShowReservationDTO> findPastForRoom(String roomId);

    void endReservation(String id);
}
