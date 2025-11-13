package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.UpdateReportResource;

public class UpdateReportCommandFromResourceAssembler {
    public static UpdateReportCommand toCommandFromResource(UpdateReportResource resource) {
        return new UpdateReportCommand(
            resource.reportId(),
            resource.latitude(),
            resource.longitude(),
            resource.containerId(),
            resource.reportType(),
            resource.description(),
            resource.resolutionNote()
        );
    }
}