package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Distance;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteType;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.RouteResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.VehicleId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DurationUtils;

public class RouteResourceFromEntityAssembler {
    public static RouteResource toResourceFromEntity(Route route) {
        return RouteResource.builder()
            .id(route.getId())
            .districtId(DistrictId.toStringOrNull(route.getDistrictId()))
            .vehicleId(VehicleId.toStringOrNull(route.getVehicleId()))
            .driverId(DriverId.toStringOrNull(route.getDriverId()))
            .routeType(RouteType.toStringOrNull(route.getRouteType()))
            .status(RouteStatus.toStringOrNull(route.getStatus()))
            .scheduledDate(DateTimeUtils.localDateToStringOrNull(route.getScheduledDate()))
            .startedAt(DateTimeUtils.localDateTimeToStringOrNull(route.getStartedAt()))
            .completedAt(DateTimeUtils.localDateTimeToStringOrNull(route.getCompletedAt()))
            .totalDistance(Distance.toStringOrNull(route.getTotalDistance()))
            .estimatedDuration(DurationUtils.durationToStringOrNull(route.getEstimatedDuration()))
            .actualDuration(DurationUtils.durationToStringOrNull(route.getActualDuration()))
            .currentLatitude(Location.latitudeAsStringOrNull(route.getCurrentLocation()))
            .currentLongitude(Location.longitudeAsStringOrNull(route.getCurrentLocation()))
            .lastLocationUpdate(DateTimeUtils.localDateTimeToStringOrNull(route.getLastLocationUpdate()))
            .createdAt(DateTimeUtils.dateToStringOrNull(route.getCreatedAt()))
            .updatedAt(DateTimeUtils.dateToStringOrNull(route.getUpdatedAt()))
            .build();
    }
}