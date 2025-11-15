package com.example.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    public Room(RoomType roomType, Integer capacity, Double basePrice) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }
    @NotNull
    @BsonId
    private ObjectId roomId;

    @NotNull
    @BsonProperty("room_type")
    private RoomType roomType;

    @NotNull
    @Min(1)
    @BsonProperty("capacity")
    private Integer capacity;

    @NotNull
    @DecimalMin("0.0")
    @BsonProperty("base_price")
    private Double basePrice;
}
