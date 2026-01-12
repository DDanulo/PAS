package com.example.repository;

import com.example.domain.Reservation;
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
public class ReservationRepository extends AbstractMongoRepository implements IRepository<Reservation> {
    private final MongoCollection<Reservation> reservations;

    public ReservationRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        super(mongoClient, rentAFieldDB);
        reservations = getRentAFieldDB().getCollection("reservations", Reservation.class);
    }

    @Override
    public void add(ClientSession session, Reservation obj) {
        reservations.insertOne(session, obj);
    }

    @Override
    public void remove(ClientSession session, ObjectId obj) {
        Bson filter = Filters.eq("_id", obj);
        reservations.deleteOne(session, filter);
    }

    @Override
    public Optional<Reservation> findById(ObjectId id) {
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(reservations.find(filter).first());
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.find().into(new ArrayList<>());
    }

    @Override
    public void update(ClientSession session, ObjectId id, Reservation obj) {

        Bson updateRoom = Updates.set("room", obj.getRoom());
        Bson updateUser = Updates.set("client", obj.getClient());
        Bson updateStartTime = Updates.set("start_time", obj.getStartTime());
        Bson updateEndTime = Updates.set("end_time", obj.getEndTime());
        Bson updatePrice = Updates.set("price", obj.getPrice());

        reservations.updateOne(session, Filters.eq("_id", id), Updates.combine(updateUser, updateRoom,
                updateStartTime, updateEndTime, updatePrice));
    }

    public List<Reservation> findByClient(ObjectId clientId) {
        Bson filter = Filters.eq("client._id", clientId);
        return reservations.find(filter).into(new ArrayList<>());
    }

    public List<Reservation> findByRoom(ObjectId roomId) {
        Bson filter = Filters.eq("room._id", roomId);
        return reservations.find(filter).into(new ArrayList<>());
    }
}
