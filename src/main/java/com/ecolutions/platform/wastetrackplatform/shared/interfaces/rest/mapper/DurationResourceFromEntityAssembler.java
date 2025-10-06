package com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.mapper;

import com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.dto.response.DurationResource;

import java.time.Duration;

public class DurationResourceFromEntityAssembler {
    public static DurationResource toResourceFromEntity(Duration entity) {
        return DurationResource.builder()
                .hours(entity.toHours())
                .minutes(entity.toMinutes())
                .seconds(entity.toSeconds())
                .build();
    }
}
