package org.example.DTO.Kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.Enums.StatusEnum;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SpotChangedEvent {
    private String eventId;
    private Long parkingId;
    private Long spotId;
    private Integer spotNumber;
    private String changedField;
    private StatusEnum oldStatus;
    private StatusEnum newStatus;
    private String licensePlate;
    private LocalDateTime changedAt;
}
