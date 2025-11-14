package com.example.model;

import com.example.domain.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ShowRoomDTO {

    private UUID id;

    @NotNull
    private RoomType roomType;

    @NotNull
    @Min(0)
    private Integer capacity;

    @NotNull
    @DecimalMin("0.0")
    private Double basePrice;
}
