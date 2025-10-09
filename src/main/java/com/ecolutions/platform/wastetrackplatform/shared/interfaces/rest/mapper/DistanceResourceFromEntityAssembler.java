package com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.mapper;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Distance;
import com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.dto.response.DistanceResource;

public class DistanceResourceFromEntityAssembler {
    public static DistanceResource toResourceFromEntity(Distance entity) {
        return DistanceResource.builder()
            .kilometers(entity.kilometers() != null ? entity.kilometers().toString() : null)
            .weight(entity.weight())
            .build();
    }
}