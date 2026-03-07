package org.example.DTO.REST.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ParkingResponse {
    private Long id;
    private String name;
    private Integer totalSpots;
    private Integer availableSpots;
    private LocalDateTime updatedAt;
}
