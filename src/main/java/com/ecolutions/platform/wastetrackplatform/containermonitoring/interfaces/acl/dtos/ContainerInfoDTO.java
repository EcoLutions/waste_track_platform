package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;

public record ContainerInfoDTO(
        String containerId,
        Location location,
        Integer fillLevelPercentage,
        String containerType,
        String status
) {
}
