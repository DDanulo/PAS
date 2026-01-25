package com.example.domain;

import com.example.model.users.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "clazz", value = "admin")
public class Admin extends User {
    public Admin(ObjectId userId, String login, String password, String firstName, String lastName, String email, Boolean isActive) {
        super(userId, login, password, firstName, lastName, email, isActive, Role.ADMIN);
    }
}
