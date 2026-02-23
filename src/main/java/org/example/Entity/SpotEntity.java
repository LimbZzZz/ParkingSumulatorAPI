package org.example.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Enums.SpotTypeEnum;
import org.example.Enums.StatusEnum;
import org.example.Enums.VehicleTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "parking_spots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "spot_number", nullable = false)
    private Integer spotNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;
    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private SpotTypeEnum spotType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_id")
    private ParkingEntity parking;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @PrePersist
    private void setCreatedAtAndUpdatedAt(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }



}
