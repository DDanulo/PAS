package com.example.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Reservations")
public class Reservation {

    public Reservation(Room room, Client client, LocalDateTime startTime, LocalDateTime endTime, Double price) {
        this.room = room;
        this.client = client;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public Reservation(Room room, Client client, LocalDateTime startTime, LocalDateTime endTime) {
        this.room = room;
        this.client = client;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = calculateActualPrice();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reservationId;

    @Version
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int version;

    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    // rozwiązanie z prezentacji
    //fetch z dokumentacji: (odrazu pobieramy dane)
    // Whether the association should be lazily loaded or must be eagerly fetched.
    // The EAGER strategy is a requirement on the persistence provider runtime that the associated entity must be eagerly fetched.
    // cascade z dokumentacji: (czy zarowno encja nadrzedna jak i podrzedna ma sie aktualizowac)
    // The operations that must be cascaded to the target of the association.
    private Room room;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Client client;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @NotNull
    @Column(name = "price")
    @DecimalMin("0.0")
    private Double price;

    public Double calculateActualPrice() {
        return client.getDiscount() * room.getBasePrice();
    }

    public double hoursReserved() {
        return (double) (endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC)) / 60 / 60;
    }

}
