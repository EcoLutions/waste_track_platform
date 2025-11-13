package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateVehicleResource;

public class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleCommand toCommandFromResource(UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(
            resource.vehicleId(),
            resource.licensePlate(),
            resource.vehicleType(),
            resource.volumeCapacity(),
            resource.weightCapacity(),
            resource.districtId(),
            resource.lastMaintenanceDate(),
            resource.nextMaintenanceDate(),
            resource.isActive()
        );
    }
}