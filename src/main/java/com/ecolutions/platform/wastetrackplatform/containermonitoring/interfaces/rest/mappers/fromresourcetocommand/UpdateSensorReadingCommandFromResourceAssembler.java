package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.UpdateSensorReadingResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;

public class UpdateSensorReadingCommandFromResourceAssembler {
    public static UpdateSensorReadingCommand toCommandFromResource(String sensorReadingId, UpdateSensorReadingResource resource) {
        return new UpdateSensorReadingCommand(
            sensorReadingId,
            resource.containerId() != null ? ContainerId.of(resource.containerId()) : null,
            resource.fillLevelPercentage(),
            resource.temperatureCelsius(),
            resource.batteryLevelPercentage()
        );
    }
}