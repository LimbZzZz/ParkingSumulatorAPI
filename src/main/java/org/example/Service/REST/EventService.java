package org.example.Service.REST;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Aspect.Annotation.LogExecutionTime;
import org.example.DTO.REST.Request.EventRequest;
import org.example.DTO.REST.Response.EventResponse;
import org.example.Entity.EventEntity;
import org.example.Entity.SpotEntity;
import org.example.Enums.EventTypeEnum;
import org.example.Exception.EventNotFoundException;
import org.example.Exception.SpotNotFoundException;
import org.example.Exception.VehicleNotFoundException;
import org.example.Repository.EventRepository;
import org.example.Repository.SpotRepository;
import org.example.Repository.VehicleRepository;
import org.example.Validation.SpotEventValidation;
import org.example.Validation.UpdatingSpotEvents;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        eventIsNull(request);
        EventEntity savedEvent = eventRepository.save(createEventEntity(request));
        updatingSpotEvents.updateSpotStatus(request);
        log.info("Событие {} успешно создано", request.getEventType());
        return mapToEventResponse(savedEvent);
    }

    @LogExecutionTime
    public EventResponse updateEvent(Long eventId, EventRequest request){
        eventIsNull(request);
        validateIdIsNull(eventId);
        EventEntity existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        EventEntity updatedEvent = eventRepository.save(existingEvent);
        log.info("Событие {} обновлено", eventId);
        return mapToEventResponse(updatedEvent);
    }

    @LogExecutionTime
    public EventResponse getEventFromId(Long eventId){
        validateIdIsNull(eventId);
        EventResponse response = mapToEventResponse(eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Ошибка: событие {} не найдено", eventId);
                    throw new EventNotFoundException(eventId);
                }));
        log.info("Событие {} получено", eventId);
        return response;
    }

    @LogExecutionTime
    public List<EventResponse> getAllEventsOnParking(Long parkingId){
        validateIdIsNull(parkingId);
        List<EventResponse> responseList = eventRepository.findByParkingId(parkingId).stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("Все события на парковке {}, получены", parkingId);
        return responseList;
    }

    @LogExecutionTime
    public List<EventResponse> getAllEventsOnAllParking(){
        List<EventResponse> responseList = eventRepository.findAll().stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("Все события на всех парковках получены");
        return responseList;
    }

    @LogExecutionTime
    public List<EventResponse> getAllEventToEventType(EventTypeEnum eventType){
        if(eventType == null){
            log.warn("Тип события является пустым, текущее значение {}", eventType);
            throw new IllegalArgumentException("Тип не может быть пустым");
        }
        List<EventResponse> responseList = eventRepository.findByEventType(eventType).stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("События с типом {}, получены", eventType);
        return responseList;
    }

    @LogExecutionTime
    public List<EventResponse> getEventToVehicleId(Long vehicleId){
        validateIdIsNull(vehicleId);
        List<EventResponse> responseList = eventRepository.findByVehicleId(vehicleId).stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("События связанные с транспортом {}, получены", vehicleId);
        return responseList;
    }

    @LogExecutionTime
    public List<EventResponse> getEventToVehicleLicensePlate(String plate){
        if(plate == null || plate.trim().equals("")){
            log.warn("ГРЗ введен неверно, текущее значение " + plate);
            throw new IllegalArgumentException("ГРЗ введен не верно");
        }
        List<EventResponse> responseList = eventRepository.findByLicensePlate(plate).stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("События связанные c транспортом с ГРЗ {}, получены", plate);
        return responseList;
    }

    public List<EventResponse> getEventFromPeriodToConcreteParking(Long parkingId, LocalDate start, LocalDate end){
        validateIdIsNull(parkingId);
        LocalDateTime startOfDay = start.atStartOfDay();
        LocalDateTime endOfDay = end.plusDays(1).atStartOfDay().minusNanos(1);
        List<EventResponse> responseList = eventRepository.findByParkingIdAndDateTimeBetween(parkingId, startOfDay, endOfDay)
                .stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("События с парковки {} за период c {} по {} получены",
                parkingId, startOfDay.getDayOfMonth(), endOfDay.getDayOfMonth());
        return responseList;
    }

    public List<EventResponse> getEventFromPeriodToAllParkings(LocalDate start, LocalDate end){
        LocalDateTime startOfDay = start.atStartOfDay();
        LocalDateTime endOfDay = end.plusDays(1).atStartOfDay().minusNanos(1);
        List<EventResponse> responseList = eventRepository.findByIdAndDateTimeBetween(startOfDay, endOfDay)
                .stream()
                .map(this::mapToEventResponse)
                .toList();
        log.info("События со всех парковок за период c {} по {} получены",
                startOfDay.getDayOfMonth(), endOfDay.getDayOfMonth());
        return responseList;
    }

    public EventResponse deleteEvent(Long eventId){
        validateIdIsNull(eventId);
        EventEntity deletedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        eventRepository.delete(deletedEvent);
        return mapToEventResponse(deletedEvent);
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

    private void validateIdIsNull(Long id){
        if(id == null || id < 0){
            log.warn("id {} введен неверно", id);
            throw new IllegalArgumentException("Значение введено не верно");
        }
    }
    private void eventIsNull(EventRequest request){
        if(request == null){
            log.warn("Ошибка создания события: request is null");
            throw new IllegalArgumentException("request is null");
        }
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
