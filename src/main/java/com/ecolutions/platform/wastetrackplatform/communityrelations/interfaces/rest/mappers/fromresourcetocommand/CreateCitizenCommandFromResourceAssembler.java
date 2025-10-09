package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateCitizenResource;

public class CreateCitizenCommandFromResourceAssembler {
    public static CreateCitizenCommand toCommandFromResource(CreateCitizenResource resource) {
        return new CreateCitizenCommand(
            resource.userId(),
            resource.districtId(),
            resource.firstName(),
            resource.lastName(),
            resource.email(),
            resource.phoneNumber()
        );
    }
}