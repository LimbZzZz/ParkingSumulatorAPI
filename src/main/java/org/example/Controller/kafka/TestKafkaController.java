package org.example.Controller.kafka;

import lombok.RequiredArgsConstructor;
import org.example.Service.Kafka.Producer.SensorEventProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestKafkaController {
    private final SensorEventProducer sensorEventProducer;

    @GetMapping("/entry")
    public String carEntryTestSend(@RequestParam(defaultValue = "1") Long sensorId,
                                  @RequestParam(defaultValue = "А123АА") String plate,
                                  @RequestParam(defaultValue = "1") Long parkingId){
        sensorEventProducer.sendEntryEvent(sensorId, plate, parkingId);
        return "Сообщение отправлено";
    }

    @GetMapping("/occupy")
    public String carTakeSpot(@RequestParam(defaultValue = "1") Long sensorId,
                              @RequestParam(defaultValue = "А123АА") String plate,
                              @RequestParam(defaultValue = "1") Long parkingId,
                              @RequestParam(defaultValue = "42") Long spotId){
        sensorEventProducer.sendTakeSpotEvent(sensorId, parkingId, spotId, plate);
        return "Сообщение отправлено";
    }
}
