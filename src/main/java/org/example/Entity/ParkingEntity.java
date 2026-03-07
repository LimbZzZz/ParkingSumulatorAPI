package org.example.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "parking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "total_spots", nullable = false)
    private Integer totalSpots;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "parking", fetch = FetchType.LAZY)
    private List<SpotEntity> spots;


    @PrePersist
    private void setCreatedAtAndUpdatedAt(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

}
