package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDriverResource;

public class UpdateDriverCommandFromResourceAssembler {
    public static UpdateDriverCommand toCommandFromResource(UpdateDriverResource resource) {
        return new UpdateDriverCommand(
            resource.driverId(),
            resource.firstName(),
            resource.lastName(),
            resource.documentNumber(),
            resource.phoneNumber(),
            resource.driverLicense(),
            resource.licenseExpiryDate()
        );
    }
}