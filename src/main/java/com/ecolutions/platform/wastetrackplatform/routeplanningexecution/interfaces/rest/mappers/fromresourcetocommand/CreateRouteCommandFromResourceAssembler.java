package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateRouteResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class CreateRouteCommandFromResourceAssembler {
    public static CreateRouteCommand toCommandFromResource(CreateRouteResource resource) {
        return new CreateRouteCommand(
            resource.districtId(),
            resource.routeType(),
            DateTimeUtils.stringToLocalDateOrNull(resource.scheduledDate())
        );
    }
}