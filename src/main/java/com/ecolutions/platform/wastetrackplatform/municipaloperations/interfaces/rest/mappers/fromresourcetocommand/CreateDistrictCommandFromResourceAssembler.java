package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.CreateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Username;

public class CreateDistrictCommandFromResourceAssembler {
    public static CreateDistrictCommand toCommandFromResource(CreateDistrictResource resource) {
        return new CreateDistrictCommand(
            resource.name(),
            resource.code(),
            EmailAddress.of(resource.primaryAdminEmail()),
            Username.of(resource.primaryAdminUsername()),
            resource.planId()
        );
    }
}