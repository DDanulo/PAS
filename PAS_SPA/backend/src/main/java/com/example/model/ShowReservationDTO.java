package com.example.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
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
