package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateDeviceCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateDeviceResource;

public class CreateDeviceCommandFromResourceAssembler {
    public static CreateDeviceCommand toCommandFromResource(CreateDeviceResource resource) {
        return new CreateDeviceCommand(resource.deviceIdentifier());
    }
}
