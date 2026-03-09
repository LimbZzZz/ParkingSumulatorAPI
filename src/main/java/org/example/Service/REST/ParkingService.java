package org.example.Service.REST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Aspect.Annotation.LogExecutionTime;
import org.example.DTO.REST.Request.ParkingRequest;
import org.example.DTO.REST.Response.ParkingResponse;
import org.example.Entity.ParkingEntity;
import org.example.Entity.SpotEntity;
import org.example.Enums.StatusEnum;
import org.example.Exception.ParkingNotFoundException;
import org.example.Exception.UpdateSpotOccupiedException;
import org.example.Repository.ParkingRepository;
import org.example.Repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingService {
    private final ParkingRepository parkingRepository;
    private final SpotRepository spotRepository;

    @LogExecutionTime
    public ParkingResponse createParking(ParkingRequest request){
        if(request == null){
            log.warn("Введен невереный запрос на создание парковки");
            throw new IllegalArgumentException("Введен неверный запрос");
        }
        ParkingEntity savedEntity = parkingRepository.save(createParkingEntity(request));
        log.info("Парковка {} успешно создана", savedEntity.getId());
        return mapToParkingResponse(savedEntity);
    }
    @LogExecutionTime
    public ParkingResponse updateSpotCountOnParking(Long parkingId, Integer newSpotsCount){
        validateIdIsNull(parkingId);
        if(newSpotsCount == null || newSpotsCount <= 0){
            throw new IllegalArgumentException("Количество парковочных мест указано не правильно");
        }

        ParkingEntity updatedEntity = parkingRepository.findById(parkingId)
                .orElseThrow(() ->{
                    log.warn("Парковка с id {} не найдена", parkingId);
                    throw new IllegalArgumentException("Парковка не найдена");
                });

        if(newSpotsCount < updatedEntity.getTotalSpots()){
            List<SpotEntity> spotToRemove = spotRepository
                    .findByParkingIdAndSpotNumberGreaterThen(parkingId, newSpotsCount);
            List<SpotEntity> occupiedSpots = spotToRemove.stream()
                    .filter(spot -> spot.getStatus() == StatusEnum.OCCUPIED)
                    .toList();
            if(!occupiedSpots.isEmpty()){
                log.warn("Нельзя изменить количество мест, среди них есть занятые");
                throw new UpdateSpotOccupiedException();
            }
            spotRepository.deleteAll(spotToRemove);
            log.info("Удаление точек с парковки {}, с номер > {}, произведено успешно",
                    parkingId, newSpotsCount);
        }
        updatedEntity.setTotalSpots(newSpotsCount);
        parkingRepository.save(updatedEntity);
        log.info("Количество мест на парковке {} успешно изменение", parkingId);
        return mapToParkingResponse(updatedEntity);
    }

    @LogExecutionTime
    public ParkingResponse renameParking(Long parkingId, String newName){
        validateIdIsNull(parkingId);
        ParkingEntity renamedEntity = parkingRepository.findById(parkingId)
                .orElseThrow(() -> {
                    log.warn("Параковки с id {} не существует", parkingId);
                    throw new ParkingNotFoundException(parkingId);
                });
        renamedEntity.setName(newName);
        parkingRepository.save(renamedEntity);
        log.info("Название изменено успешно, новое название {}", renamedEntity.getName());
        return mapToParkingResponse(renamedEntity);
    }

    @LogExecutionTime
    public ParkingResponse getParkingById(Long parkingId){
        validateIdIsNull(parkingId);
        ParkingResponse response =  mapToParkingResponse(parkingRepository.findById(parkingId)
                .orElseThrow(() -> {
                    log.warn("Параковки с id {} не существует", parkingId);
                    throw new ParkingNotFoundException(parkingId);
                }));
        log.info("Парковка {} успешно получена", parkingId);
        return response;
    }

    @LogExecutionTime
    public List<ParkingResponse> getAllParking(){
        List<ParkingResponse> parkingList = parkingRepository.findAll().stream()
                .map(this::mapToParkingResponse)
                .toList();
        log.info("Парковки получены успешно");
        return parkingList;
    }

    @LogExecutionTime
    public Map<Long, List<Integer>> availableSpotsOnAllParking(){
        List<ParkingEntity> parkingIdList = parkingRepository.findAll();
        Map<Long, List<Integer>> availableSpotsMap = new HashMap<>();
        for (ParkingEntity parkingEntity : parkingIdList) {
            availableSpotsMap.put(parkingEntity.getId(), parkingEntity.getSpots().stream()
                    .filter(spot -> !spot.getStatus().equals(StatusEnum.OCCUPIED))
                    .map(SpotEntity::getSpotNumber)
                    .toList());
        }
        log.info("Доступные места на всех парковках успешно получены");
        return availableSpotsMap;
    }

    @LogExecutionTime
    public void deleteParking(Long parkingId){
        validateIdIsNull(parkingId);
        parkingRepository.deleteById(parkingId);
        log.info("Парковка {} успешно удалена", parkingId);
    }

    private ParkingEntity createParkingEntity(ParkingRequest request){
        return ParkingEntity.builder()
                .name(request.getName())
                .totalSpots(request.getTotalSpots())
                .build();
    }

    private ParkingResponse mapToParkingResponse(ParkingEntity entity){
        return ParkingResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .totalSpots(entity.getTotalSpots())
                .availableSpots(getAvailableSpots(entity.getId()))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @LogExecutionTime
    public Integer getAvailableSpots(Long parkingId){
        validateIdIsNull(parkingId);
        ParkingEntity entity = parkingRepository.findById(parkingId)
                .orElseThrow(() -> {
                    log.warn("Парковка с id {}, не найдена", parkingId);
                    throw new ParkingNotFoundException(parkingId);
                });
        return entity.getTotalSpots() - spotRepository.countByParkingId(parkingId);
    }

    private void validateIdIsNull(Long id){
        if(id == null || id < 0){
            log.warn("Id введен не правильно, текущее значение {}", id);
            throw new IllegalArgumentException("Id введен не правильно");
        }
    }
}
