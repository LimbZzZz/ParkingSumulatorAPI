package org.example.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Enums.SensorTypeEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "sensor_type", nullable = false)
    private SensorTypeEnum sensorType;
    @Column(name = "location", nullable = false)
    private String sensorLocation;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_id")
    private ParkingEntity parking;

    @PrePersist
    private void setDate(){
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }
}
