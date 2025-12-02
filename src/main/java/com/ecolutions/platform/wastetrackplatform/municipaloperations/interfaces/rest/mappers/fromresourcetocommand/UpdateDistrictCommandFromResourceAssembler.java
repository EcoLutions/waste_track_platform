package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request.UpdateDistrictResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DurationUtils;

public class UpdateDistrictCommandFromResourceAssembler {
    public static UpdateDistrictCommand toCommandFromResource(String districtId, UpdateDistrictResource resource) {
        return new UpdateDistrictCommand(
            districtId,
            resource.name(),
            resource.code(),
            resource.depotLatitud(),
            resource.depotLongitude(),
            resource.disposalLatitude(),
            resource.disposalLongitude(),
            DateTimeUtils.stringToLocalTimeOrNull(resource.operationStartTime()),
            DateTimeUtils.stringToLocalTimeOrNull(resource.operationEndTime()),
            DurationUtils.stringToDurationOrNull(resource.maxRouteDuration())
        );
    }
}