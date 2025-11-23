package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Report;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllReportsByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllReportsQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetReportByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ReportQueryService {
    Optional<Report> handle(GetReportByIdQuery query);
    List<Report> handle(GetAllReportsQuery query);
    List<Report> handle(GetAllReportsByDistrictIdQuery query);
}