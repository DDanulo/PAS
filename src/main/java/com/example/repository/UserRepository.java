package com.example.repository;

import com.example.domain.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository implements IRepository<Client> {

    public Client findByLogin;

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

    /*@Override
    public Optional<Client> findById(UUID obj, EntityManager em) {
        return Optional.ofNullable(em.find(Client.class, obj));
    } */

    @Override
    public List<Client> findAll(EntityManager em) {
        return null;
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
