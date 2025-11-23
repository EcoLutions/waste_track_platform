package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Report;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.ReportType;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.ReportCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.ReportRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.CitizenId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportCommandServiceImpl implements ReportCommandService {
    private final ReportRepository reportRepository;

    @Override
    public Optional<Report> handle(CreateReportCommand command) {
        try {
            var citizenId = new CitizenId(command.citizenId());
            var location = Location.fromStrings(command.latitude(), command.longitude());
            var containerId = ContainerId.of(command.containerId());
            var districtId = DistrictId.of(command.districtId());
            var reportType = ReportType.fromString(command.reportType());

            var newReport = new Report(
                citizenId,
                districtId,
                location,
                reportType,
                containerId,
                command.description()
            );
            var savedReport = reportRepository.save(newReport);
            return Optional.of(savedReport);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create report: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Report> handle(UpdateReportCommand command) {
        try {
            Report existingReport = reportRepository.findById(command.reportId())
                    .orElseThrow(() -> new IllegalArgumentException("Report with ID " + command.reportId() + " not found."));

            if (command.latitude() != null && command.longitude() != null) {
                var newLocation = Location.fromStrings(command.latitude(), command.longitude());
                existingReport.setLocation(newLocation);
            }

            if (command.containerId() != null) {
                existingReport.setContainerId(ContainerId.of(command.containerId()));
            }

            if (command.reportType() != null) {
                existingReport.setReportType(ReportType.fromString(command.reportType()));
            }

            if (command.description() != null) {
                existingReport.setDescription(command.description());
            }

            // TODO: Only update status and resolution note if the user has admin privileges
            if (command.resolutionNote() != null) {
                existingReport.setResolutionNote(command.resolutionNote());
            }

            var updatedReport = reportRepository.save(existingReport);
            return Optional.of(updatedReport);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update report: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteReportCommand command) {
        try {
            Report existingReport = reportRepository.findById(command.reportId())
                    .orElseThrow(() -> new IllegalArgumentException("Report with ID " + command.reportId() + " not found."));

            reportRepository.delete(existingReport);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete report: " + e.getMessage(), e);
        }
    }
}