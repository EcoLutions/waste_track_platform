package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.UpdateContainerResource;

public class UpdateContainerCommandFromResourceAssembler {
    public static UpdateContainerCommand toCommandFromResource(UpdateContainerResource resource) {
        return new UpdateContainerCommand(
            resource.containerId(),
            resource.latitude(),
            resource.longitude(),
            resource.volumeLiters(),
            resource.maxWeightKg(),
            resource.sensorId(),
            resource.containerType(),
            resource.districtId(),
            resource.collectionFrequencyDays()
        );
    }
}