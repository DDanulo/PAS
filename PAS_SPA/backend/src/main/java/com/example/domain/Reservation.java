package com.example.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @NotNull
    @BsonId
    private ObjectId reservationId;

    @NotNull
    @BsonProperty("room")
    private Room room;

    @NotNull
    @BsonProperty("client")
    private Client client;

    @NotNull
    @BsonProperty("start_time")
    private LocalDateTime startTime;

    @NotNull
    @BsonProperty("end_time")
    private LocalDateTime endTime;

    @BsonProperty("price")
    private Double price;

    public Double calculateActualPrice() {
        return room.getBasePrice();
    }

    public double hoursReserved() {
        return (double) (endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC)) / 60 / 60;
    }

    public Reservation(ObjectId reservationId, Room room, Client client, LocalDateTime startTime, Double price) {
        this.reservationId = reservationId;
        this.room = room;
        this.client = client;
        this.startTime = startTime;
        this.price = price;
    }
}
