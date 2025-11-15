package com.example.service;

import com.example.domain.Reservation;
import com.example.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final EntityManager em;
    private final EntityTransaction transaction;

    public void makeReservation(Reservation res) {
        try {
            LocalDateTime start = res.getStartTime();
            LocalDateTime end = res.getEndTime();
            List<Reservation> allReservations = getAllReservations();

            if (start.isAfter(end) || start.equals(end)) {
                throw new Exception("Czas zakończenia rezerwacji jest niepoprawny.");
            }

            for (Reservation r : allReservations) {
                if (res.getRoom().equals(r.getRoom())) {
                    boolean timeCollision = start.isBefore(r.getEndTime()) && end.isAfter(r.getStartTime());

                    if (timeCollision) {
                        throw new Exception("W tym czasie istnieje już inna rezerwacja.");
                    }
                }
            }

            transaction.begin();
            repository.add(res, em);
            transaction.commit();
            System.out.println("Dodano nową rezerwację.");
        } catch (Exception e) {
            System.out.println("Nie można dokonać rezerwacji: " + e.getMessage());
        }
    }

    public void cancelReservation(UUID id) {
        transaction.begin();
        repository.removeById(id, em);
        transaction.commit();
    }
/*
    public Reservation findReservation(UUID id) {
        return repository.findById(id, em);
    } */

    public List<Reservation> getAllReservations() {
        return repository.findAll(em);
    }

    public void updateReservation(Reservation res, UUID id, LocalDateTime newStart, LocalDateTime newEnd) {
        try {
            List<Reservation> allReservations = getAllReservations();

            if (newStart.isAfter(newEnd) || newStart.equals(newEnd)) {
                throw new Exception("Czas zakończenia rezerwacji jest niepoprawny.");
            }

            for (Reservation r : allReservations) {
                if(res.getReservationId().equals(r.getReservationId())) {
                    continue;
                }

                if (res.getRoom().equals(r.getRoom())) {
                    boolean timeCollision = newStart.isBefore(r.getEndTime()) && newEnd.isAfter(r.getStartTime());

                    if (timeCollision) {
                        throw new Exception("W tym czasie istnieje już inna rezerwacja.");
                    }
                }
            }

            res.setStartTime(newStart);
            res.setEndTime(newEnd);
            res.setRoom(res.getRoom());
            res.setClient(res.getClient());
            transaction.begin();
            repository.updateElement(res, id, em);
            transaction.commit();
            System.out.println("Zaaktualizowano rezerwację.");
        }
        catch (Exception e) {
            System.out.println("Nie zaaktualizować rezerwacji: " + e.getMessage());
        }

    }
}
