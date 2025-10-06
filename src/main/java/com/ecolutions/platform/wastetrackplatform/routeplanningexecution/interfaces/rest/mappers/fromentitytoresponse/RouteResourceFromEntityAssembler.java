package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.RouteResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.VehicleId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;
import com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.mapper.DistanceResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.shared.interfaces.rest.mapper.DurationResourceFromEntityAssembler;

public class RouteResourceFromEntityAssembler {
    public static RouteResource toResourceFromEntity(Route route) {
        return RouteResource.builder()
            .id(route.getId())
            .districtId(DistrictId.toStringOrNull(route.getDistrictId()))
            .vehicleId(VehicleId.toStringOrNull(route.getVehicleId()))
            .driverId(DriverId.toStringOrNull(route.getDriverId()))
            .routeType(route.getRouteType() != null ? route.getRouteType().name() : null)
            .status(route.getStatus() != null ? route.getStatus().name() : null)
            .scheduledDate(DateTimeUtils.localDateToStringOrNull(route.getScheduledDate()))
            .startedAt(DateTimeUtils.localDateTimeToStringOrNull(route.getStartedAt()))
            .completedAt(DateTimeUtils.localDateTimeToStringOrNull(route.getCompletedAt()))
            .totalDistance(route.getTotalDistance() != null ? DistanceResourceFromEntityAssembler.toResourceFromEntity(route.getTotalDistance()) : null)
            .estimatedDuration(route.getEstimatedDuration() != null ? DurationResourceFromEntityAssembler.toResourceFromEntity(route.getEstimatedDuration()) : null)
            .actualDuration(route.getActualDuration() != null ? DurationResourceFromEntityAssembler.toResourceFromEntity(route.getActualDuration()) : null)
            .createdAt(DateTimeUtils.dateToStringOrNull(route.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(route.getUpdatedAt()))
            .build();
    }
}