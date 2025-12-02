package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Report;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllReportsByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllReportsQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetReportByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries.ReportQueryService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.ReportRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {
    private final ReportRepository reportRepository;

    @Override
    public Optional<Report> handle(GetReportByIdQuery query) {
        try {
            return reportRepository.findById(query.reportId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve report: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Report> handle(GetAllReportsQuery query) {
        try {
            return reportRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve reports: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Report> handle(GetAllReportsByDistrictIdQuery query) {
        try {
            return reportRepository.findByDistrictId(new DistrictId(query.districtId()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve reports by district ID: " + e.getMessage(), e);
        }
    }

}