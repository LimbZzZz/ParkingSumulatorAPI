package org.example.Service.REST;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Aspect.Annotation.LogExecutionTime;
import org.example.DTO.REST.Request.EventRequest;
import org.example.DTO.REST.Response.EventResponse;
import org.example.Entity.EventEntity;
import org.example.Entity.SpotEntity;
import org.example.Exception.SpotNotFoundException;
import org.example.Exception.VehicleNotFoundException;
import org.example.Repository.EventRepository;
import org.example.Repository.SpotRepository;
import org.example.Repository.VehicleRepository;
import org.example.Validation.SpotEventValidation;
import org.example.Validation.UpdatingSpotEvents;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;
    private final SpotEventValidation spotEventValidation;
    private final UpdatingSpotEvents updatingSpotEvents;

    @LogExecutionTime
    public EventResponse createEvent(EventRequest request){
        if(request == null){
            log.warn("Ошибка создания события: request is null");
            throw new IllegalArgumentException("request is null");
        }
        EventEntity savedEvent = eventRepository.save(createEventEntity(request));
        updatingSpotEvents.updateSpotStatus(request);
        return mapToEventResponse(savedEvent);
    }

    private EventEntity createEventEntity(EventRequest request){
        SpotEntity spot = spotRepository.findById(request.getSpotId())
                .orElseThrow(() -> new SpotNotFoundException(request.getSpotId()));

        spotEventValidation.validateSpotForEvent(request.getEventType(), spot);

        return EventEntity.builder()
                .eventType(request.getEventType())
                .vehicle(vehicleRepository.findById(request.getVehicleId())
                        .orElseThrow(() -> {
                            log.warn("Транспортное средство с id: {}, не найдено", request.getVehicleId());
                            throw new VehicleNotFoundException(request.getVehicleId());
                        }))
                .spot(spot)
                .build();
    }

    private EventResponse mapToEventResponse(EventEntity entity){
        return EventResponse.builder()
                .id(entity.getId())
                .eventType(entity.getEventType().toString())
                .vehicleId(entity.getVehicle().getId())
                .vehiclePlate(entity.getVehicle().getLicensePlate())
                .spotId(entity.getSpot().getId())
                .spotType(entity.getSpot().getSpotType().toString())
                .spotNumber(entity.getSpot().getSpotNumber())
                .dateTime(entity.getDateTime())
                .build();
    }

}
