package com.example.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "student_user")
public class StudentClient extends Client {

    public StudentClient(String login, String firstName, String lastName, String email) {
        super(login, true, firstName, lastName, email);
    }

    @Override
    public double getDiscount() {
        return 0.25;
    }

}
