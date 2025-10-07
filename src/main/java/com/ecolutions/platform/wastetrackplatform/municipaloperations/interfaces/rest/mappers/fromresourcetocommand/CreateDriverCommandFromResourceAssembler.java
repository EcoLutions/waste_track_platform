package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateDriverResource;

public class CreateDriverCommandFromResourceAssembler {
    public static CreateDriverCommand toCommandFromResource(CreateDriverResource resource) {
        return new CreateDriverCommand(
            resource.districtId(),
            resource.firstName(),
            resource.lastName(),
            resource.documentNumber(),
            resource.phoneNumber(),
            resource.userId(),
            resource.driverLicense(),
            resource.licenseExpiryDate(),
            resource.emailAddress()
        );
    }
}