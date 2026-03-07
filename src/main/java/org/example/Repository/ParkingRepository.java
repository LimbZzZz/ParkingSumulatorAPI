package org.example.Repository;

import org.example.Entity.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<ParkingEntity, Long> {
}
