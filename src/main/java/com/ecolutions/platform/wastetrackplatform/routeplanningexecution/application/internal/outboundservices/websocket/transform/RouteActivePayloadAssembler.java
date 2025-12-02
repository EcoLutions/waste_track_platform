package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.RouteActivePayload;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.VehicleId;

public class RouteActivePayloadAssembler {
    public static RouteActivePayload toPayload(Route route) {
        return RouteActivePayload.builder()
                .routeId(route.getId())
                .vehicleId(VehicleId.toStringOrNull(route.getVehicleId()))
                .driverId(DriverId.toStringOrNull(route.getDriverId()))
                .build();
    }
}
