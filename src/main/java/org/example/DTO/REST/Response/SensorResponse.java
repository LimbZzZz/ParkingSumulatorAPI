package org.example.DTO.REST.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SensorResponse {
    private Long id;
    private String name;
    private String sensorType;
    private String sensorLocation;
    private Boolean isActive;
    private Long parkingId;
    private String parkingName;
}
