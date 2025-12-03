package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.payloads;

import lombok.Builder;

@Builder
public record ContainerUpdatedFillLevelPayload(
    String containerId,
    Integer fillLevelPercentage
) {}
