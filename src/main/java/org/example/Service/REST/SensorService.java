package org.example.Service.REST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.REST.Request.SensorRequest;
import org.example.DTO.REST.Response.SensorResponse;
import org.example.Entity.SensorEntity;
import org.example.Entity.SpotEntity;
import org.example.Exception.ParkingNotFoundException;
import org.example.Exception.SensorNotFoundException;
import org.example.Exception.SpotNotFoundException;
import org.example.Repository.ParkingRepository;
import org.example.Repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final ParkingRepository parkingRepository;

    public SensorResponse createSensor(SensorRequest request){
        if(request == null){
            log.warn("Неверный запрос");
            throw new IllegalArgumentException("Запрос не может быть null");
        }
        SensorEntity createdEntity = createSensorEntity(request);
        sensorRepository.save(createdEntity);
        log.info("Сенсор {} успешно добавлен на парковку {}", request.getName(), request.getParkingId());
        return mapToSensorResponse(createdEntity);
    }

    public SensorResponse getSensorById(Long sensorId){
        SensorResponse response = mapToSensorResponse(validateSensorFinding(sensorId));
        log.info("Сенсор {} успешно получен", sensorId);
        return response;
    }

    public List<SensorResponse> getAllSensors(){
        List<SensorResponse> sensorsList = sensorRepository.findAll().stream()
                .map(this::mapToSensorResponse)
                .toList();
        log.info("Сенсоры успешно получены");
        return sensorsList;
    }

    private SensorEntity createSensorEntity(SensorRequest request){
        return SensorEntity.builder()
                .name(request.getName())
                .sensorType(request.getSensorType())
                .sensorLocation(request.getSensorLocation())
                .isActive(request.isActive())
                .parking(parkingRepository.findById(request.getParkingId())
                        .orElseThrow(() ->{
                            log.info("Парковки с id {} не существует", request.getParkingId());
                            throw new ParkingNotFoundException(request.getParkingId());
                        }))
                .build();
    }

    private SensorEntity validateSensorFinding(Long sensorId){
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> {
                    log.warn("Cенсора с id {}, не существует", sensorId);
                    throw new SensorNotFoundException(sensorId);
                });
    }
    private SensorResponse mapToSensorResponse(SensorEntity entity){
        return SensorResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .sensorType(entity.getSensorType().toString())
                .sensorLocation(entity.getSensorLocation())
                .isActive(entity.getIsActive())
                .parkingId(entity.getParking().getId())
                .parkingName(entity.getParking().getName())
                .build();
    }
}
