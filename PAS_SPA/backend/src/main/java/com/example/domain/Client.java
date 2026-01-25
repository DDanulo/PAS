package com.example.domain;

import com.example.model.users.Role;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "clazz", value = "client")
public class Client extends User {
    public Client(String login, String password, String firstName, String lastName, String email) {
        super(null, login, password, firstName, lastName, email, true, Role.CLIENT);
    }

    public Client(ObjectId userId, String login, String password, String firstName, String lastName, String email, Boolean isActive) {
        super(userId, login, password, firstName, lastName, email, isActive, Role.CLIENT);
    }
}