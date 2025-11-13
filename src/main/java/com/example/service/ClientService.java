package com.example.service;

import com.example.domain.Client;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


import java.util.List;
import java.util.UUID;

public class ClientService {

    private final UserRepository repository;
    private final EntityManager em;
    private final EntityTransaction transaction;

    public ClientService(EntityManager em, EntityTransaction transaction) {
        repository = new UserRepository();
        this.em = em;
        this.transaction = transaction;
    }

    public void registerUser(Client client) {
        transaction.begin();
        repository.add(client, em);
        transaction.commit();
    }

    public Client findUsers(UUID id) {
        return repository.findById(id, em);
    }

    public List<Client> getAllUsers() {
        return repository.findAll(em);
    }

    public void removeUser(UUID id) {
        transaction.begin();
        repository.removeById(id, em);
        transaction.commit();
    }
    public void updateUser(Client client, UUID id){
        transaction.begin();
        repository.updateElement(client, id, em);
        transaction.commit();
    }
}
