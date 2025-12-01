package com.example.repository;

import com.example.domain.Room;
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
public class RoomRepository extends AbstractMongoRepository /*implements IRepository<Room>*/ {

    private final MongoCollection<Room> rooms;

    public RoomRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        super(mongoClient, rentAFieldDB);
        rooms = getRentAFieldDB().getCollection("rooms", Room.class);
    }

//    @Override
    public Room add(ClientSession session, Room obj) {
        rooms.insertOne(session, obj);
        return obj;
    }

//    @Override
    public void remove(ClientSession session, ObjectId obj) {
        Bson filter = Filters.eq("_id", obj);
        rooms.deleteOne(session, filter);
    }

//    @Override
    public Optional<Room> findById(ObjectId id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(rooms.find(filter).first());
    }

//    @Override
    public List<Room> findAll() {
        return rooms.find().into(new ArrayList<>());
    }

//    @Override
    public Room update(ClientSession session, ObjectId id, Room obj) {
        Bson updateCapacity = Updates.set("capacity", obj.getCapacity());
        Bson updatePrice = Updates.set("base_price", obj.getBasePrice());
        Bson updateRoomType = Updates.set("room_type", obj.getRoomType());
        rooms.updateOne(session, Filters.eq("_id", id), Updates.combine(updatePrice, updateCapacity, updateRoomType));
        return obj;
    }
}
