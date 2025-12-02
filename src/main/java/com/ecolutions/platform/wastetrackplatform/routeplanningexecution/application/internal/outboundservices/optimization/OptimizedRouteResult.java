package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.optimization;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Distance;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OptimizedRouteResult(
        List<WayPoint> waypoints,
        Duration totalDuration,
        Duration collectionDuration,
        Duration returnDuration,
        Distance totalDistance,
        LocalDateTime scheduledEndAt,
        String encodedPolyline
) {
}
