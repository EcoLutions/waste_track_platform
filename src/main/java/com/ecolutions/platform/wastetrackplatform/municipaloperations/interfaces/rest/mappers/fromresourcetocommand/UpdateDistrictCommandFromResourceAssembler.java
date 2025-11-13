package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDistrictResource;

public class UpdateDistrictCommandFromResourceAssembler {
    public static UpdateDistrictCommand toCommandFromResource(UpdateDistrictResource resource) {
        return new UpdateDistrictCommand(
            resource.districtId(),
            resource.name(),
            resource.code(),
            resource.depotLatitud(),
            resource.depotLongitude()
        );
    }
}