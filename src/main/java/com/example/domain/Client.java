package com.example.domain;

import lombok.*;

@Getter
@Setter
public class Client extends User {
    public Client(String login, String firstName, String lastName, String email) {
        super(login, firstName, lastName, email);
    }
}