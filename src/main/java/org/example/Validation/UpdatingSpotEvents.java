package org.example.Validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.REST.Request.EventRequest;
import org.example.Entity.SpotEntity;
import org.example.Enums.StatusEnum;
import org.example.Exception.SpotNotFoundException;
import org.example.Repository.SpotRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatingSpotEvents {

    private final SpotRepository spotRepository;

    public void updateSpotStatus(EventRequest request){
        SpotEntity spot = spotRepository.findById(request.getSpotId())
                .orElseThrow(() -> new SpotNotFoundException(request.getSpotId()));

        switch (request.getEventType()){
            case RESERVATION: {
                spot.setStatus(StatusEnum.RESERVED);
                log.info("Место {} зарезервировано", spot.getSpotNumber());
                break;
            }
            case EXIT:{
                spot.setStatus(StatusEnum.FREE);
                spot.setVehicle(null);
                log.info("Место {} освобождено (выезд)", spot.getSpotNumber());
                break;
            }
            case CANCELLATION: {
                spot.setStatus(StatusEnum.FREE);
                spot.setVehicle(null);
                log.info("Бронь места {} отменена", spot.getSpotNumber());
                break;
            }
            case ENTRY:{
                log.debug("Въезд - статус места {} не меняется", spot.getSpotNumber());
                return;
            }

            default:{
                log.debug("Событие {} не требует обновления статуса места", request.getEventType());
                return;
            }
        }

        spotRepository.save(spot);
    }
}
