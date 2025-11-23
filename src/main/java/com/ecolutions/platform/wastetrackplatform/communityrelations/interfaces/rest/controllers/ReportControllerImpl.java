package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteReportCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllReportsByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllReportsQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetReportByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.ReportCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries.ReportQueryService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateReportResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.UpdateReportResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.ReportResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromentitytoresponse.ReportResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand.CreateReportCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand.UpdateReportCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.swagger.ReportController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportControllerImpl implements ReportController {
    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;

    @Override
    public ResponseEntity<ReportResource> createReport(CreateReportResource resource) {
        var command = CreateReportCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdReport = reportCommandService.handle(command);
        if (createdReport.isEmpty()) return ResponseEntity.badRequest().build();
        var reportResource = ReportResourceFromEntityAssembler.toResourceFromEntity(createdReport.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reportResource.id())
                .toUri();
        return ResponseEntity.created(location).body(reportResource);
    }

    @Override
    public ResponseEntity<ReportResource> getReportById(String id) {
        var query = new GetReportByIdQuery(id);
        var report = reportQueryService.handle(query);
        if (report.isEmpty()) return ResponseEntity.notFound().build();
        var reportResource = ReportResourceFromEntityAssembler.toResourceFromEntity(report.get());
        return ResponseEntity.ok(reportResource);
    }

    @Override
    public ResponseEntity<List<ReportResource>> getAllReports() {
        var query = new GetAllReportsQuery();
        var reports = reportQueryService.handle(query);
        var reportResources = reports.stream()
                .map(ReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(reportResources);
    }

    @Override
    public ResponseEntity<List<ReportResource>> getAllReportsByDistrictId(String districtId) {
        var query = new GetAllReportsByDistrictIdQuery(districtId);
        var reports = reportQueryService.handle(query);
        var reportResources = reports.stream()
                .map(ReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(reportResources);
    }

    @Override
    public ResponseEntity<ReportResource> updateReport(String id, UpdateReportResource resource) {
        var command = UpdateReportCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedReport = reportCommandService.handle(command);
        if (updatedReport.isEmpty()) return ResponseEntity.notFound().build();
        var reportResource = ReportResourceFromEntityAssembler.toResourceFromEntity(updatedReport.get());
        return ResponseEntity.ok(reportResource);
    }

    @Override
    public ResponseEntity<Void> deleteReport(String id) {
        var command = new DeleteReportCommand(id);
        var deleted = reportCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}