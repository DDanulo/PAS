package com.example.repository;

import com.mongodb.client.ClientSession;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void add(ClientSession session, T obj);

    void remove(ClientSession session, ObjectId obj);

    Optional<T> findById(ObjectId obj);

    List<T> findAll();

    void update(ClientSession session, ObjectId id, T obj);}
