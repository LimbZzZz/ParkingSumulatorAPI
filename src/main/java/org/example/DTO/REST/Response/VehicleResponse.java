package org.example.DTO.REST.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VehicleResponse {
    private Long id;
    private String licensePlate;
    private String vehicleType;
    private Long spotId;
    private Integer spotNumber;
}
