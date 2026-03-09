package org.example.DTO.REST.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.Enums.EventTypeEnum;

@Data
@Builder
@AllArgsConstructor
public class EventRequest {
    private EventTypeEnum eventType;
    @NotNull
    @Positive
    private Long vehicleId;
    @NotNull
    @Positive
    private Long spotId;
    @NotNull
    @Positive
    private Long parkingId;
}
