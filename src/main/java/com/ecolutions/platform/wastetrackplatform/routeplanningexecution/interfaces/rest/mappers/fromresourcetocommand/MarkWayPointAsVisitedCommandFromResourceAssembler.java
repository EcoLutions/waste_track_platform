package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.MarkWayPointAsVisitedCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.MarkWayPointAsVisitedResource;

public class MarkWayPointAsVisitedCommandFromResourceAssembler {
    public static MarkWayPointAsVisitedCommand toCommandFromResource(String waypointId, MarkWayPointAsVisitedResource resource) {
        return new MarkWayPointAsVisitedCommand(
            resource.routeId(),
            waypointId,
            resource.arrivalTime()
        );
    }
}

