package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.UpdateCitizenResource;

public class UpdateCitizenCommandFromResourceAssembler {
    public static UpdateCitizenCommand toCommandFromResource(UpdateCitizenResource resource) {
        return new UpdateCitizenCommand(
            resource.citizenId(),
            resource.districtId(),
            resource.firstName(),
            resource.lastName(),
            resource.email(),
            resource.phoneNumber()
        );
    }
}