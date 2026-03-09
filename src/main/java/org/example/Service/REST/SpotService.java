package org.example.Service.REST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Aspect.Annotation.LogExecutionTime;
import org.example.DTO.REST.Request.SpotRequest;
import org.example.DTO.REST.Response.SpotResponse;
import org.example.Entity.ParkingEntity;
import org.example.Entity.SpotEntity;
import org.example.Enums.SpotTypeEnum;
import org.example.Enums.StatusEnum;
import org.example.Exception.ParkingNotFoundException;
import org.example.Exception.SpotAlreadyOccupiedException;
import org.example.Exception.SpotNotFoundException;
import org.example.Exception.VehicleNotFoundException;
import org.example.Repository.ParkingRepository;
import org.example.Repository.SpotRepository;
import org.example.Repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final ParkingRepository parkingRepository;
    private final VehicleRepository vehicleRepository;

    private SpotResponse createSpot(SpotRequest request){
        if(request == null){
            log.warn("Ошибка, запрос составлен неверно");
            throw new IllegalArgumentException("Неверный тип запроса");
        }
        SpotEntity savedSpot = spotRepository.save(createSpotEntity(request));
        log.info("Точка {} создана на парковке {}, транспорт {}",
                savedSpot.getSpotNumber(),
                savedSpot.getParking().getId(),
                savedSpot.getVehicle().getLicensePlate());
        return mapToSpotResponse(savedSpot);
    }

    private SpotResponse updateSpot(Long spotId, Long vehicleId, StatusEnum status){
        SpotEntity spot = validateSpotFinding(spotId);
        spot.setStatus(status);
        spot.setVehicle(vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.warn("Транспорта с id {} не существует", vehicleId);
                    throw new VehicleNotFoundException(vehicleId);
                }));
        spot.setUpdatedAt(LocalDateTime.now());

        spotRepository.save(spot);
        log.info("Точка {} успешно обновлена", spotId);
        return mapToSpotResponse(spot);
    }


    @LogExecutionTime
    public void reservationParkingSpot(Long parkingId, Integer spotNumber, Long vehicleId){
        validateIdIsNull(parkingId);
        validateIdIsNull(vehicleId);
        validateSpotNumber(spotNumber);
        ParkingEntity parking = parkingRepository.findById(parkingId)
                .orElseThrow(() ->{
                    log.warn("Парковки с id {} не существует", parkingId);
                    throw new ParkingNotFoundException(parkingId);
                });
        if(spotNumber < 0 || spotNumber > parking.getTotalSpots()){
            log.warn("Некорректный номер места");
            throw new IllegalArgumentException("Номер места введен неверно");
        }

        SpotEntity spot = spotRepository.findByParkingIdAndSpotNumber(parkingId, spotNumber);
        if(spot == null){
            createSpot(new SpotRequest(spotNumber, StatusEnum.RESERVED,
                    SpotTypeEnum.REGULAR, parkingId, vehicleId));
        }else{
            if(spot.getStatus().equals(StatusEnum.OCCUPIED)){
                log.warn("Место {} на парковке {} уже занято", spotNumber, parkingId);
                throw new SpotAlreadyOccupiedException(spotNumber);
            }

            updateSpot(spot.getId(), vehicleId, StatusEnum.RESERVED);

            log.info("Место для транспорта {} успешно забронировано. Парковка: {}, место: {}",
                    vehicleId, parkingId, spotNumber);
        }
    }

    @LogExecutionTime
    public SpotResponse takeTheSpot(Long parkingId, Integer spotNumber, Long vehicleId){
        validateIdIsNull(parkingId);
        validateSpotNumber(spotNumber);
        SpotEntity spot = spotRepository.findByParkingIdAndSpotNumber(parkingId, spotNumber);
        if(spot.getStatus() != StatusEnum.FREE){
            log.warn("Место {} на парковке {} занято", spotNumber, parkingId);
            throw new IllegalArgumentException("Место занято, выберите другое место");
        }
        log.info("Место {} успешно занято машиной {}", spotNumber, vehicleId);
        return updateSpot(spot.getId(), vehicleId, StatusEnum.OCCUPIED);
    }

    @LogExecutionTime
    public void deletedSpot(Long parkingId, Integer spotNumber){
        validateIdIsNull(parkingId);
        validateSpotNumber(spotNumber);
        SpotEntity deletedSpot = spotRepository.findByParkingIdAndSpotNumber(parkingId,spotNumber);
        if(deletedSpot.getStatus().equals(StatusEnum.RESERVED) || deletedSpot.getStatus().equals(StatusEnum.OCCUPIED)){
            log.warn("Нельзя удалить место {}, так как оно занято машиной {}", deletedSpot.getSpotNumber(), deletedSpot.getVehicle().getLicensePlate());
            throw new SpotAlreadyOccupiedException(deletedSpot.getSpotNumber());
        }
        spotRepository.delete(deletedSpot);
        log.info("Место {} на парковке {} успешно удалено", deletedSpot.getSpotNumber(), parkingId);
    }

    @LogExecutionTime
    public SpotResponse getSpotInfoOnParking(Long parkingId, Integer spotNumber){
        validateIdIsNull(parkingId);
        validateSpotNumber(spotNumber);
        SpotEntity spot = spotRepository.findByParkingIdAndSpotNumber(parkingId, spotNumber);
        log.info("Информация о месте {} на парковке {} успешно получена", spotNumber, parkingId);
        return mapToSpotResponse(spot);
    }

    @LogExecutionTime
    public List<SpotResponse> getSpotsInfoOnAllParking(){
        List<SpotResponse> spotsList = spotRepository.findAll().stream()
                .map(this::mapToSpotResponse)
                .toList();
        log.info("Информация о всех местах успешно получена");
        return spotsList;
    }

    @LogExecutionTime
    public List<SpotResponse> getSpotsInfoOnConcreteParking(Long parkingId){
        List<SpotResponse> spotsList = spotRepository.findByParkingId(parkingId).stream()
                .map(this::mapToSpotResponse)
                .toList();
        log.info("Информация о всех местах на парковке {} успешно получена", parkingId);
        return spotsList;
    }

    private SpotEntity createSpotEntity(SpotRequest request){
        return SpotEntity.builder()
                .spotNumber(request.getSpotNumber())
                .status(request.getStatus())
                .spotType(request.getSpotType())
                .parking(parkingRepository.findById(request.getParkingId())
                        .orElseThrow(() ->{
                            log.warn("Парковки с id {} не существует", request.getParkingId());
                            throw new ParkingNotFoundException(request.getParkingId());
                        }))
                .vehicle(vehicleRepository.findById(request.getVehicleId())
                        .orElseThrow(() ->{
                            log.warn("Транспорта с id {} не существует", request.getVehicleId());
                            throw new VehicleNotFoundException(request.getVehicleId());
                        }))
                .build();
    }

    private void validateIdIsNull(Long id){
        if(id == null || id < 0){
            log.warn("Id указан неверно, текущее значение {}", id);
            throw new IllegalArgumentException("Неверное значение id");
        }
    }

    private SpotEntity validateSpotFinding(Long spotId){
        return spotRepository.findById(spotId)
                .orElseThrow(() -> {
                    log.warn("Точки с id {}, не существует", spotId);
                    throw new SpotNotFoundException(spotId);
                });
    }

    private void validateSpotNumber(Integer spotNumber){
        if(spotNumber == null || spotNumber < 0){
            log.warn("Место указано не правильно");
            throw new IllegalArgumentException("Место указано не правильно");
        }
    }

    private SpotResponse mapToSpotResponse(SpotEntity spot){
        return SpotResponse.builder()
                .id(spot.getId())
                .spotNumber(spot.getSpotNumber())
                .spotType(spot.getSpotType().toString())
                .updatedAt(spot.getUpdatedAt())
                .parkingId(spot.getParking().getId())
                .parkingName(spot.getParking().getName())
                .vehicleId(spot.getVehicle().getId())
                .vehiclePlate(spot.getVehicle().getLicensePlate())
                .build();
    }
}
