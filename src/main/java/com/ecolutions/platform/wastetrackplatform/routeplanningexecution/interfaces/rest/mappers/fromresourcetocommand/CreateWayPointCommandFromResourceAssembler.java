package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateWayPointResource;

public class CreateWayPointCommandFromResourceAssembler {
    public static CreateWayPointCommand toCommandFromResource(CreateWayPointResource resource, String routeId) {
        return new CreateWayPointCommand(
            routeId,
            resource.containerId(),
            resource.sequenceOrder(),
            resource.priority()
        );
    }
}