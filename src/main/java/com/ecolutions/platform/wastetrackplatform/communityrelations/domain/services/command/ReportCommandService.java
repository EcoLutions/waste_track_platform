package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Report;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateReportCommand;

import java.util.Optional;

public interface ReportCommandService {
    Optional<Report> handle(CreateReportCommand command);
    Optional<Report> handle(UpdateReportCommand command);
    Boolean handle(DeleteReportCommand command);
}