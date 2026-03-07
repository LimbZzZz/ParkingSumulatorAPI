package org.example.Service.Kafka.Producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.Kafka.SensorChangedEvent;
import org.example.Enums.EventTypeEnum;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventProducer {
    private final KafkaTemplate<String, SensorChangedEvent> kafkaTemplate;
    @Value("${kafka.template.default-topic:parking-events}")
    private String defaultTopic;

    public void sendEntryEvent(Long sensorId, String licensePlate, Long parkingId){
        SensorChangedEvent sensorEvent = SensorChangedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .sensorId(sensorId)
                .eventType(EventTypeEnum.ENTRY)
                .licensePlate(licensePlate)
                .parkingId(parkingId)
                .timeStamp(LocalDateTime.now())
                .build();

        createEvent(sensorEvent, parkingId);
    }

    public void sendTakeSpotEvent(Long sensorId, Long parkingId, Long spotId, String licensePlate){
        SensorChangedEvent sensorEvent = SensorChangedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .sensorId(sensorId)
                .parkingId(parkingId)
                .spotId(spotId)
                .eventType(EventTypeEnum.OCCUPIED)
                .licensePlate(licensePlate)
                .timeStamp(LocalDateTime.now())
                .build();

        createEvent(sensorEvent, parkingId);
    }

    private void createEvent(SensorChangedEvent sensorEvent, Long parkingId){
        kafkaTemplate.send(defaultTopic, parkingId.toString(), sensorEvent)
                .whenComplete((result, ex) -> {
                    if(ex == null){
                        log.info("Событие {} успешно отправлено в топик {}, партиция {}, offset {}",
                                sensorEvent.getEventId(),
                                defaultTopic,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }else{
                        log.error("Ошибка отправки события {}: {}",
                                sensorEvent.getEventId(),
                                ex.getMessage());
                    }
                    log.info("Событие отправлено: {}", sensorEvent);
                });
    }

}
