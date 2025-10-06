package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.GeographicBoundaries;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;

public class UpdateDistrictCommandFromResourceAssembler {
    public static UpdateDistrictCommand toCommandFromResource(UpdateDistrictResource resource) {
        return new UpdateDistrictCommand(
            resource.districtId(),
            resource.name(),
            resource.code(),
            new GeographicBoundaries(resource.boundaries()),
            EmailAddress.of(resource.primaryAdminEmail())
        );
    }
}