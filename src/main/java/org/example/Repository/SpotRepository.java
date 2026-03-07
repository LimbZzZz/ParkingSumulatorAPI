package org.example.Repository;

import org.example.Entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<SpotEntity, Long> {
}
