package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateWayPointResource;

public class UpdateWayPointCommandFromResourceAssembler {
    public static UpdateWayPointCommand toCommandFromResource(String wayPointId, UpdateWayPointResource resource) {
        return new UpdateWayPointCommand(
            wayPointId,
            resource.sequenceOrder(),
            resource.priority(),
            resource.estimatedArrivalTime()
        );
    }
}