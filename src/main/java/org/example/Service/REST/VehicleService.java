package org.example.Service.REST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Aspect.Annotation.LogExecutionTime;
import org.example.DTO.REST.Request.VehicleRequest;
import org.example.DTO.REST.Response.VehicleResponse;
import org.example.Entity.SpotEntity;
import org.example.Entity.VehicleEntity;
import org.example.Exception.SpotNotFoundException;
import org.example.Exception.VehicleNotFoundException;
import org.example.Repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @LogExecutionTime
    public VehicleResponse createVehicle(VehicleRequest request){
        if(request == null){
            log.warn("Ошибка, запрос составлен неверно");
            throw new IllegalArgumentException("Неверный тип запроса");
        }
        VehicleEntity createdEntity = createVehicleEntity(request);
        VehicleResponse response = mapToVehicleResponse(vehicleRepository.save(createdEntity));
        log.info("Машина создана, ГРЗ: {}", request.getVehiclePlate());
        return response;
    }

    @LogExecutionTime
    public VehicleResponse updateLicensePlate(Long vehicleId, String newLicensePlate){
        validateIdIsNull(vehicleId);
        if(newLicensePlate == null){
            log.warn("Неверный формат ввода ГРЗ");
            throw new IllegalArgumentException("Неверный формат ввода ГРЗ");
        }
        VehicleEntity updatedVehicle = validateIdFinding(vehicleId);
        updatedVehicle.setLicensePlate(newLicensePlate);
        vehicleRepository.save(updatedVehicle);
        log.info("ГРЗ машины {} машины успешно обновлен на {}", vehicleId, newLicensePlate);
        return mapToVehicleResponse(updatedVehicle);
    }

    @LogExecutionTime
    public VehicleResponse getVehicle(Long vehicleId){
        validateIdIsNull(vehicleId);
        VehicleResponse response = mapToVehicleResponse(validateIdFinding(vehicleId));
        log.info("Машина {} успешно получена", vehicleId);
        return response;
    }

    @LogExecutionTime
    public List<VehicleResponse> getAllVehicles(){
        List<VehicleResponse> responseList = vehicleRepository.findAll().stream()
                .map(this::mapToVehicleResponse)
                .toList();
        log.info("Машины успешно получены");
        return responseList;
    }

    @LogExecutionTime
    public List<VehicleResponse> getAllVehiclesFromParking(Long parkingId){
        List<VehicleResponse> responseList = vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getSpot().getParking().getId().equals(parkingId))
                .map(this::mapToVehicleResponse)
                .toList();
        log.info("Машины с парковки {} успешно получены", parkingId);
        return responseList;
    }

    @LogExecutionTime
    public void deleteVehicle(Long vehicleId){
        validateIdIsNull(vehicleId);
        vehicleRepository.deleteById(vehicleId);
        log.info("Машина {} успешно удалена", vehicleId);
    }
    private VehicleEntity createVehicleEntity(VehicleRequest request){
        return VehicleEntity.builder()
                .licensePlate(request.getVehiclePlate())
                .vehicleType(request.getVehicleType())
                .build();
    }

    private void validateIdIsNull(Long id){
        if(id == null || id < 0){
            log.warn("Id указан неверно, текущее значение {}", id);
            throw new IllegalArgumentException("Неверное значение id");
        }
    }

    private VehicleEntity validateIdFinding(Long vehicleId){
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.warn("Машины с id {}, не существует", vehicleId);
                    throw new VehicleNotFoundException(vehicleId);
                });
    }
    private VehicleResponse mapToVehicleResponse(VehicleEntity entity){
        return VehicleResponse.builder()
                .id(entity.getId())
                .licensePlate(entity.getLicensePlate())
                .vehicleType(entity.getVehicleType().toString())
                .spotId(entity.getSpot().getId())
                .spotNumber(entity.getSpot().getSpotNumber())
                .build();
    }
}
