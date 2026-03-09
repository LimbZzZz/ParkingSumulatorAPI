package org.example.DTO.Kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ParkingChangedEvent {
    private String eventId;
    private Long parkingId;
    private String changedField;
    private String oldValue;
    private String newValue;
    private LocalDateTime changedAt;
}
