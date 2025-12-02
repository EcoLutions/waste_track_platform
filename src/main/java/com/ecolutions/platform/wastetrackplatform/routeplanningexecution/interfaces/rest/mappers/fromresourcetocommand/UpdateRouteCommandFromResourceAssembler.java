package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateRouteResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class UpdateRouteCommandFromResourceAssembler {
    public static UpdateRouteCommand toCommandFromResource(UpdateRouteResource resource) {
        return new UpdateRouteCommand(
            resource.routeId(),
            DateTimeUtils.stringToLocalDateTimeOrNull(resource.scheduledStartAt())
        );
    }
}