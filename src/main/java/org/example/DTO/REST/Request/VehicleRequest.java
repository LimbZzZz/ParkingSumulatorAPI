package org.example.DTO.REST.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.Enums.VehicleTypeEnum;

@Data
@Builder
@AllArgsConstructor
public class VehicleRequest {
    @NotNull
    @NotBlank
    private String vehiclePlate;
    @NotNull
    @NotBlank
    private VehicleTypeEnum vehicleType;
}
