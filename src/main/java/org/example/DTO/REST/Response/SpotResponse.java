package org.example.DTO.REST.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SpotResponse {
    private Long id;
    private Integer spotNumber;
    private String status;
    private String spotType;
    private LocalDateTime updatedAt;
    private Long parkingId;
    private String parkingName;
    private Long vehicleId;
    private String vehiclePlate;
}
