package org.example.DTO.REST.Request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.Enums.SpotTypeEnum;
import org.example.Enums.StatusEnum;


@Data
@Builder
@AllArgsConstructor
public class SpotRequest {
    @Positive
    private Integer spotNumber;
    @NotNull
    private StatusEnum status;
    @NotNull
    private SpotTypeEnum spotType;
    @NotNull
    @Positive
    private Long parkingId;
    @Nullable
    @Positive
    private Long vehicleId;
}
