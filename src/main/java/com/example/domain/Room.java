package com.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
@Table(name = "Rooms")
public class Room {

    public Room(RoomType roomType, Integer capacity, Double basePrice) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roomId;

    @Version
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private int version;

    @CreationTimestamp
    private LocalDateTime dateCreated;
    @UpdateTimestamp
    private LocalDateTime dateUpdated;


    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @NotNull
    @Column(name = "capacity")
    @Min(0)
    private Integer capacity;

    @NotNull
    @Column(name = "base_price")
    @DecimalMin("0.0")
    private Double basePrice;
}
