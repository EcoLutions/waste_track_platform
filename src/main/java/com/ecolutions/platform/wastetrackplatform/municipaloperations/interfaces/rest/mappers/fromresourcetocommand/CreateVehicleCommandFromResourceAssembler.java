package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateVehicleResource;

import java.math.BigDecimal;

public class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(CreateVehicleResource resource) {
        return new CreateVehicleCommand(
            resource.licensePlate(),
            resource.vehicleType(),
            new BigDecimal(resource.volumeCapacity()),
            new BigDecimal(resource.weightCapacity()),
            resource.districtId()
        );
    }
}