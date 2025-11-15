package com.example.repository;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepository<T> {
    void add(T obj, EntityManager em);

    void removeById(UUID obj, EntityManager em);

   // Optional<T> findById(UUID obj, EntityManager em);

    List<T> findAll(EntityManager em);

    void updateElement(T newElement, UUID id, EntityManager em);
}
