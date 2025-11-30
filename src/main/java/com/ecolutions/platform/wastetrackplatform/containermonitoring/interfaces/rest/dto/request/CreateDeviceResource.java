package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record CreateDeviceResource(String deviceIdentifier) {
    public CreateDeviceResource {
        if (deviceIdentifier == null || deviceIdentifier.isBlank()) {
            throw new IllegalArgumentException("Device identifier cannot be null or blank");
        }
    }
}
