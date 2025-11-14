package com.example.repository;

import com.example.domain.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomRepository implements IRepository<Room> {
    @Override
    public void add(Room obj, EntityManager em) {
        em.persist(obj);
    }

    @Override
    public void removeById(UUID obj, EntityManager em) {
        Room room = em.find(Room.class, obj, LockModeType.PESSIMISTIC_WRITE);
        if (room != null) {
            em.remove(room);
        }
    }

    @Override
    public Optional<Room> findById(UUID obj, EntityManager em) {
        return Optional.ofNullable(em.find(Room.class, obj));
    }

    @Override
    public List<Room> findAll(EntityManager em) {
        return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }

    @Override
    public void updateElement(Room newElement, UUID id, EntityManager em) {
        Room room = em.find(Room.class, id, LockModeType.PESSIMISTIC_WRITE);
        room.setRoomType(newElement.getRoomType());
        room.setBasePrice(newElement.getBasePrice());
        room.setCapacity(newElement.getCapacity());
        em.persist(room);
    }
}
