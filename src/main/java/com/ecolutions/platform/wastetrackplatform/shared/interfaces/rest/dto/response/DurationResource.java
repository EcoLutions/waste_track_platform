package com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record DurationResource(
    Long hours,
    Long minutes,
    Long seconds
) {
}
