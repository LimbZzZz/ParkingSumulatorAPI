package org.example.Service.Kafka.Consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorEventConsumer {
    @KafkaListener(topics = "parking-events")

}
