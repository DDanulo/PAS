package com.example.repository;

import com.mongodb.client.MongoDatabase;
import lombok.Getter;

@Getter
public abstract class AbstractMongoRepository implements AutoCloseable {
    protected MongoDatabase rentAFieldDB;

    public AbstractMongoRepository(MongoDatabase rentAFieldDB) {
        this.rentAFieldDB = rentAFieldDB;
    }

    public AbstractMongoRepository() {
    }

    @Override
    public void close() {
    }
}