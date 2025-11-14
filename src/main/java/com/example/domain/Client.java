package com.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Client extends User {

    public Client(String login, Boolean isActive, String firstName, String lastName, String email) {
        super(login, isActive);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @NotNull
    @Column(name = "first_name", length = 64)
    private String firstName;

    @NotNull
    @Column(name = "last_name", length = 64)
    private String lastName;

    @NotNull
    @Column(name = "email", length = 64)
    private String email;

    @Version
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Integer version;


    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;


    public abstract double getDiscount();

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
