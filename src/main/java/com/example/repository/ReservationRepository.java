package com.example.repository;

import com.example.domain.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepository implements IRepository<Reservation> {
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

    /*@Override
    public Optional<Reservation> findById(UUID obj) {
        return em.find(Reservation.class, obj);
    }*/

    @Override
    public List<Reservation> findAll(EntityManager em) {
        return null;
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
