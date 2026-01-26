package com.example.config;

import com.example.domain.*;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@Configuration
public class MongoConfig {

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        return fromProviders(
                PojoCodecProvider.builder()
                        .automatic(true)
                        .register(User.class, Admin.class, Moderator.class, Client.class,
                                Reservation.class, Room.class)
//                        .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                        .build()
        );
    }

    @Bean
    public MongoClient mongoClient(CodecRegistry pojoCodecRegistry) {
        ConnectionString connectionString = new ConnectionString(
                "mongodb://mongodb1:27017/?replicaSet=replica_set_single&authSource=admin");

        MongoCredential credential = MongoCredential.createCredential(
                "admin", "admin", "adminpassword".toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabase rentAFieldDB(MongoClient mongoClient) {
        return mongoClient.getDatabase("rentafield");
    }

    @PostConstruct
    public void ensureIndexesAndDrop() {
        MongoCollection<User> users = rentAFieldDB(mongoClient(pojoCodecRegistry())).getCollection("users", User.class);
        users.drop();
        MongoCollection<Reservation> reservations = rentAFieldDB(mongoClient(pojoCodecRegistry())).getCollection("reservations", Reservation.class);
        reservations.drop();
        MongoCollection<Room> rooms = rentAFieldDB(mongoClient(pojoCodecRegistry())).getCollection("rooms", Room.class);
//        rooms.drop();
        users.createIndex(
                Indexes.ascending("login"),
                new IndexOptions().unique(true).name("uk_users_login")
        );
    }
}