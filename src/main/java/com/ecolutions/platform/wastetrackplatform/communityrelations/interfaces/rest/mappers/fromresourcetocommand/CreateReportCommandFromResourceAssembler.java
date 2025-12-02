package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateReportResource;

public class CreateReportCommandFromResourceAssembler {
    public static CreateReportCommand toCommandFromResource(CreateReportResource resource) {
        return new CreateReportCommand(
            resource.citizenId(),
            resource.districtId(),
            resource.latitude(),
            resource.longitude(),
            resource.containerId(),
            resource.reportType(),
            resource.description(),
            resource.evidenceIds()
        );
    }
}