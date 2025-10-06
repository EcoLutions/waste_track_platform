package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateSensorReadingResource;

public class CreateSensorReadingCommandFromResourceAssembler {
    public static CreateSensorReadingCommand toCommandFromResource(CreateSensorReadingResource resource) {
        return new CreateSensorReadingCommand(
            resource.containerId(),
            resource.fillLevelPercentage(),
            resource.temperatureCelsius(),
            resource.batteryLevelPercentage()
        );
    }
}