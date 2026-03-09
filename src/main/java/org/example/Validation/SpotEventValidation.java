package org.example.Validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Entity.SpotEntity;
import org.example.Enums.EventTypeEnum;
import org.example.Enums.StatusEnum;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class SpotEventValidation {

    public void validateSpotForEvent(EventTypeEnum eventType, SpotEntity spot){
        switch (eventType){
            case RESERVATION:{
                if (spot.getStatus() != StatusEnum.FREE){
                    log.warn("Место {} занято. Текущий статус: {}",
                            spot.getSpotNumber(), spot.getStatus());
                    throw new IllegalArgumentException("Место занято");
                }
                break;
            }
            case CANCELLATION:{
                if(spot.getStatus() != StatusEnum.RESERVED){
                    log.warn("Место {} не зарезервировано. Текущий статус: {}",
                            spot.getSpotNumber(), spot.getStatus());
                    throw new IllegalArgumentException("Место не зарезервировано");
                }
                break;
            }
            case EXIT:{
                if(spot.getStatus() != StatusEnum.OCCUPIED){
                    log.warn("Место {} не занято. Текущий статус: {}",
                            spot.getSpotNumber(), spot.getStatus());
                    throw new IllegalArgumentException("Место не занято");
                }
                break;
            }

            default:
                log.debug("Событие {} не требует проверки статуса места", eventType);
        }
    }
}
