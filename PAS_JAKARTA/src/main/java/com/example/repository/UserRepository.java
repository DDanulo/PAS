package com.example.repository;

import com.example.domain.User;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements IRepository<User> {

    private MongoCollection<User> users;

    public UserRepository() {
    }

    @Inject
    public UserRepository(MongoDatabase rentAFieldDB) {
        this.users = rentAFieldDB.getCollection("users", User.class);
    }

    @Override
    public void add(ClientSession session, User obj) {
        users.insertOne(session, obj);
    }

    @Override
    public void remove(ClientSession session, ObjectId obj) {
        Bson filter = Filters.eq("_id", obj);
        users.deleteOne(session, filter);
    }

    @Override
    public Optional<User> findById(ObjectId id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(users.find(filter).first());
    }

    @Override
    public List<User> findAll() {
        return users.find().into(new ArrayList<>());
    }

    @Override
    public void update(ClientSession session, ObjectId id, User obj) {
        Bson updateName = Updates.set("first_name", obj.getFirstName());
        Bson updateLastName = Updates.set("last_name", obj.getLastName());
        Bson updateEmail = Updates.set("email", obj.getEmail());
        users.updateOne(session, Filters.eq("_id", id),
                Updates.combine(updateEmail, updateName, updateLastName));
    }

    public void setActiveStatus(ClientSession session, ObjectId id) {
        Bson updateStatus = Updates.set("is_active", true);
        if (session == null) {
            users.updateOne(Filters.eq("_id", id), updateStatus);
        } else {
            users.updateOne(session, Filters.eq("_id", id), updateStatus);
        }
    }

    public void setInactiveStatus(ClientSession session, ObjectId id) {
        Bson updateStatus = Updates.set("is_active", false);
        if (session == null) {
            users.updateOne(Filters.eq("_id", id), updateStatus);
        } else {
            users.updateOne(session, Filters.eq("_id", id), updateStatus);
        }
    }

    public Optional<User> findByLogin(String login) {
        Bson filter = Filters.eq("login", login);
        return Optional.ofNullable(users.find(filter).first());
    }

    public List<User> searchByLogin(String login) {
        Bson filter = Filters.regex("login", ".*" + login + ".*");
        return users.find(filter).into(new ArrayList<>());
    }
}
