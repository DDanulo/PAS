package com.example.config;

import com.example.domain.*;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.context.Dependent;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@ApplicationScoped
public class MongoConfig {
    @Produces
    @ApplicationScoped
    public CodecRegistry pojoCodecRegistry() {
        return fromProviders(
                PojoCodecProvider.builder()
                        .automatic(true)
                        .register(User.class, Admin.class, Moderator.class, Client.class,
                                Reservation.class, Room.class)
                        .build()
        );
    }

    @Produces
    @ApplicationScoped
    public MongoClient mongoClient(CodecRegistry pojoCodecRegistry) {
        String connectionStringUrl = "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single&authSource=admin";

        ConnectionString connectionString = new ConnectionString(connectionStringUrl);

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

    @Produces
    @ApplicationScoped
    public MongoDatabase rentAFieldDB(MongoClient mongoClient) {
        return mongoClient.getDatabase("rentafield");
    }
}