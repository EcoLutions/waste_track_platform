package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.RouteCurrentLocationUpdatedPayload;

import java.time.LocalDateTime;

public class RouteCurrentLocationPayloadAssembler {
    public static RouteCurrentLocationUpdatedPayload toPayload(Route route) {
        if (route.getCurrentLocation() == null) {
            throw new IllegalArgumentException("Route current location cannot be null");
        }

        long remainingWaypoints = route.getWaypoints().stream()
                .filter(wp -> wp.getStatus() == com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.WaypointStatus.PENDING)
                .count();

        String estimatedCompletion = route.getScheduledEndAt() != null
                ? route.getScheduledEndAt().toString()
                : null;

        return RouteCurrentLocationUpdatedPayload.builder()
                .routeId(route.getId())
                .latitude(route.getCurrentLocation().latitude().toString())
                .longitude(route.getCurrentLocation().longitude().toString())
                .timestamp(LocalDateTime.now())
                .status(route.getStatus().name())
                .remainingWaypoints((int) remainingWaypoints)
                .estimatedCompletionTime(estimatedCompletion)
                .build();
    }
}
