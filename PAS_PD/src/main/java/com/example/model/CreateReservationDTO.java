package com.example.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationDTO {

    @NotNull
    private String roomId;

    @NotNull
    private String clientId;

    @NotNull
    private LocalDateTime startTime;


//    @NotNull
    @DecimalMin("0.0")
    private Double price;

}
