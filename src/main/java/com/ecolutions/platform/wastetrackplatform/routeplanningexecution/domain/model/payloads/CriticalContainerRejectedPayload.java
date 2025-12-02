package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CriticalContainerRejectedPayload(
        String routeId,
        String containerId,
        String latitude,
        String longitude,
        Integer fillLevel,
        String reason,
        String action,
        LocalDateTime timestamp
) {}