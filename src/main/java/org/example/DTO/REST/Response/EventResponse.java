package org.example.DTO.REST.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String eventType;
    private Long vehicleId;
    private String vehiclePlate;
    private Long spotId;
    private String spotType;
    private Integer spotNumber;
    private LocalDateTime dateTime;
}
