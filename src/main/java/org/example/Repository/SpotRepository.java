package org.example.Repository;

import org.example.Entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotRepository extends JpaRepository<SpotEntity, Long> {
    Integer countByParkingId(Long parkingId);
    List<SpotEntity> findByParkingId(Long parkingId);
    List<SpotEntity> findByParkingIdAndSpotNumberGreaterThen(Long parkingId, Integer count);
    SpotEntity findByParkingIdAndSpotNumber(Long parkingId, Integer spotNumber);
}
