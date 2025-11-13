package com.example.repository;

import com.example.domain.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.UUID;

public class ReservationRepository implements Repository<Reservation> {
    @Override
    public void add(Reservation obj, EntityManager em) {
        em.persist(obj);
    }

    @Override
    public void removeById(UUID obj, EntityManager em) {
        Reservation reservation = em.find(Reservation.class, obj, LockModeType.PESSIMISTIC_WRITE);
        if (reservation != null) {
            em.remove(reservation);
        }
    }

    @Override
    public Reservation findById(UUID obj, EntityManager em) {
        return em.find(Reservation.class, obj);
    }

    @Override
    public List<Reservation> findAll(EntityManager em) {
        return em.createQuery("SELECT r FROM Reservation r", Reservation.class).getResultList();
    }

    @Override
    public void updateElement(Reservation newElement, UUID id, EntityManager em) {
        Reservation reservation = em.find(Reservation.class, id, LockModeType.PESSIMISTIC_WRITE);
        reservation.setRoom(newElement.getRoom());
        reservation.setStartTime(newElement.getStartTime());
        reservation.setEndTime(newElement.getEndTime());
        reservation.setPrice(newElement.getPrice());
    }
}
