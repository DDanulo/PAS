package com.example.repository;

import com.example.domain.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;


import java.util.List;
import java.util.UUID;

public class UserRepository implements Repository<Client> {
    @Override
    public void add(Client obj, EntityManager em) {
        em.persist(obj);
    }

    @Override
    public void removeById(UUID obj, EntityManager em) {
        Client client = em.find(Client.class, obj, LockModeType.PESSIMISTIC_WRITE);
        if (client != null) {
            em.remove(client);
        }
    }

    @Override
    public Client findById(UUID obj, EntityManager em) {
        return em.find(Client.class, obj);
    }

    @Override
    public List<Client> findAll(EntityManager em) {
        return em.createQuery("SELECT u FROM Client u", Client.class).getResultList();
    }

    @Override
    public void updateElement(Client newElement, UUID id, EntityManager em) {
        Client client = em.find(Client.class, id, LockModeType.PESSIMISTIC_WRITE);
        client.setEmail(newElement.getEmail());
        client.setFirstName(newElement.getFirstName());
        client.setLastName(newElement.getLastName());
        em.persist(client);
    }
}
