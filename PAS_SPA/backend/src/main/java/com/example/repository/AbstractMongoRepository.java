package com.example.repository;

import com.example.domain.*;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

@Getter
public abstract class AbstractMongoRepository implements AutoCloseable {
//
//    private final ConnectionString connectionString = new ConnectionString(
//            "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single"
//    );
//
//    private final MongoCredential credential = MongoCredential.createCredential(
//            "admin", "admin", "adminpassword".toCharArray()
//    );
//
//    private final CodecRegistry pojoCodecRegistry = fromProviders(
//            PojoCodecProvider.builder()
//                    .automatic(true)
//                    .register(User.class, Client.class, Moderator.class, Admin.class,
//                            Reservation.class, Room.class)
//                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
//                    .build()
//    );

    protected MongoClient mongoClient;
    protected MongoDatabase rentAFieldDB;

    public AbstractMongoRepository(MongoClient mongoClient, MongoDatabase rentAFieldDB) {
        this.mongoClient = mongoClient;
        this.rentAFieldDB = rentAFieldDB;
    }

    //    public AbstractMongoRepository() {
//        initDbConnection();
//    }
//
//    private void initDbConnection() {
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .credential(credential)
//                .applyConnectionString(connectionString)
//                .uuidRepresentation(UuidRepresentation.STANDARD)
//                .codecRegistry(CodecRegistries.fromRegistries(
//                        MongoClientSettings.getDefaultCodecRegistry(),
//                        pojoCodecRegistry
//                ))
//                .build();
//
//        mongoClient = MongoClients.create(settings);
//        rentAFieldDB = mongoClient.getDatabase("rentafield");
//    }

    public ClientSession startSession() {
        return mongoClient.startSession();
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
