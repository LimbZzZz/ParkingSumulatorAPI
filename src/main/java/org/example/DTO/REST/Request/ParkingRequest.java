package org.example.DTO.REST.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParkingRequest {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Positive
    private Integer totalSpots;
}
