package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Report;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.ReportStatus;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.ReportType;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.ReportResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class ReportResourceFromEntityAssembler {
    public static ReportResource toResourceFromEntity(Report entity) {
        return ReportResource.builder()
                .id(entity.getId())
                .citizenId(CitizenId.toStringOrNull(entity.getCitizenId()))
                .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
                .latitude(Location.latitudeAsStringOrNull(entity.getLocation()))
                .longitude(Location.longitudeAsStringOrNull(entity.getLocation()))
                .containerId(ContainerId.toStringOrNull(entity.getContainerId()))
                .reportType(ReportType.toStringOrNull(entity.getReportType()))
                .description(entity.getDescription())
                .status(ReportStatus.toStringOrNull(entity.getStatus()))
                .resolutionNote(entity.getResolutionNote())
                .resolvedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getResolvedAt()))
                .resolvedBy(UserId.toStringOrNull(entity.getResolvedBy()))
                .submittedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getSubmittedAt()))
                .acknowledgedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getAcknowledgedAt()))
                .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
                .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
                .build();
    }
}