package com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record DistanceResource(
    String kilometers,
    Integer weight
) {
}