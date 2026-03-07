package org.example.Repository;

import org.example.Entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
}
