package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.rest.dto.request.CreateContainerResource;

public class CreateContainerCommandFromResourceAssembler {
    public static CreateContainerCommand toCommandFromResource(CreateContainerResource resource) {
        return new CreateContainerCommand(
            resource.latitude(),
            resource.longitude(),
            resource.volumeLiters(),
            resource.maxFillLevel(),
            resource.deviceId(),
            resource.containerType(),
            resource.districtId(),
            resource.collectionFrequencyDays()
        );
    }
}