package com.example.repository;

import com.example.domain.User;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends AbstractMongoRepository implements IRepository<User> {

    private final MongoCollection<User> users;

    public UserRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        super(mongoClient, rentAFieldDB);
        users = getRentAFieldDB().getCollection("users", User.class);
    }

    @Override
    public void add(ClientSession session, User obj) {
        users.insertOne(session, obj);
    }

    @Override
    public void remove(ClientSession session, ObjectId id) {
        users.deleteOne(session, Filters.eq("_id", id));
    }

    @Override
    public Optional<User> findById(ObjectId id) {
        return Optional.ofNullable(users.find(Filters.eq("_id", id)).first());
    }

    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.find(Filters.eq("login", login)).first());
    }

    public List<User> searchByLogin(String login) {
        Bson filter = Filters.regex("login", ".*" + login + ".*", "i");
        return users.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<User> findAll() {
        return users.find().into(new ArrayList<>());
    }

    @Override
    public void update(ClientSession session, ObjectId id, User obj) {
        List<Bson> updated = new ArrayList<>();

        updated.add(Updates.set("login", obj.getLogin()));
        updated.add(Updates.set("first_name", obj.getFirstName()));
        updated.add(Updates.set("last_name", obj.getLastName()));
        updated.add(Updates.set("email", obj.getEmail()));
        updated.add(Updates.set("role", obj.getRole()));
        updated.add(Updates.set("active", obj.getIsActive()));

        if (obj.getPassword() != null) {
            updated.add(Updates.set("password", obj.getPassword()));
        }

        users.updateOne(session, Filters.eq("_id", id), Updates.combine(updated));
    }

    public void activateAccount(ClientSession session, ObjectId id) {
        users.updateOne(session != null ? session : getMongoClient().startSession(),
                Filters.eq("_id", id),
                Updates.set("active", true));
    }

    public void deactivateAccount(ClientSession session, ObjectId id) {
        users.updateOne(session != null ? session : getMongoClient().startSession(),
                Filters.eq("_id", id),
                Updates.set("active", false));
    }
}