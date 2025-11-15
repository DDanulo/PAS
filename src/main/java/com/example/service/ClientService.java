package com.example.service;

import com.example.domain.Client;
import com.example.mappers.UserMapper;
import com.example.model.ShowUserDTO;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final UserMapper userMapper;
    private final UserRepository repository;
    private final EntityManager em;
    private final EntityTransaction transaction;


    public void registerUser(Client client) {
        transaction.begin();
        repository.add(client, em);
        transaction.commit();
    }
/*
    public Client findUsers(UUID id) {
        return repository.findById(id, em);
    }

    public List<ShowUserDTO> getAllUsers() {
        return repository.findAll(em).stream().map(userMapper::roomToShowRoomDTO).toList();
    } */

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
