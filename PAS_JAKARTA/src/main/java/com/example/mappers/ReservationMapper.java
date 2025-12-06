package com.example.mappers;

import com.example.domain.Reservation;
import com.example.model.CreateReservationDTO;
import com.example.model.ShowReservationDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReservationMapper {
    public Reservation createReservationDTOToReservation(CreateReservationDTO reservationDTO) {
        return Reservation.builder()
                .price(reservationDTO.getPrice())
                .startTime(reservationDTO.getStartTime())
                .build();
    }

    public ShowReservationDTO reservationToShowReservationDTO(Reservation reservation) {
        return ShowReservationDTO.builder()
                .reservationId(reservation.getReservationId().toString())
                .clientId(reservation.getClient().getUserId().toString())
                .roomId(reservation.getRoom().getRoomId().toString())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .price(reservation.getPrice())
                .build();
    }
}
