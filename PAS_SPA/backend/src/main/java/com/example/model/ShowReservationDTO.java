package com.example.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation = "reservation", itemRelation = "reservation")
public class ShowReservationDTO extends RepresentationModel<ShowReservationDTO> {

    private String reservationId;

    @NotNull
    private String roomId;

    @NotNull
    private String clientId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private Double price;
}
