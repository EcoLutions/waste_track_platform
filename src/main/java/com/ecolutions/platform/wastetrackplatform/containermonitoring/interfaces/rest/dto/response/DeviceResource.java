package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record DeviceResource(
        String id,
        String deviceIdentifier,
        String createdAt,
        String updatedAt
) {
}
