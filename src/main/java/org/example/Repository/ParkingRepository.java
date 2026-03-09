package org.example.Repository;

import org.example.Entity.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingRepository extends JpaRepository<ParkingEntity, Long> {

}
