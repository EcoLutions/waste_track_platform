package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContainerBecameCriticalEvent(
        String containerId,
        String districtId,
        Location location,
        Integer fillLevel,
        LocalDateTime timestamp
) {
}
