package org.example.DTO.REST.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.Enums.SensorTypeEnum;

@Data
@Builder
@AllArgsConstructor
public class SensorRequest {
    @NotBlank
    private String name;
    @NotNull
    private SensorTypeEnum sensorType;
    @NotNull
    private String sensorLocation;
    @NotNull
    private boolean isActive;
    @NotNull
    @Positive
    private Long parkingId;
}
