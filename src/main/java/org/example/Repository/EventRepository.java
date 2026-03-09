package org.example.Repository;

import org.example.Entity.EventEntity;
import org.example.Enums.EventTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByParkingId(Long parkingId);
    List<EventEntity> findByEventType(EventTypeEnum eventType);
    List<EventEntity> findByVehicleId(Long vehicleId);
    List<EventEntity> findByLicensePlate(String plate);
    List<EventEntity> findByParkingIdAndDateTimeBetween(Long parkingId, LocalDateTime start, LocalDateTime end);
    List<EventEntity> findByIdAndDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
