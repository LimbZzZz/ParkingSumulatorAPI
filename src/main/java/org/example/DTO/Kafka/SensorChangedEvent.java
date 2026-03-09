package org.example.DTO.Kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.Enums.EventTypeEnum;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SensorChangedEvent {
    private String eventId;
    private Long sensorId;
    private Long parkingId;
    private Long spotId;
    private EventTypeEnum eventType;
    private String licensePlate;
    private LocalDateTime timeStamp;
}
