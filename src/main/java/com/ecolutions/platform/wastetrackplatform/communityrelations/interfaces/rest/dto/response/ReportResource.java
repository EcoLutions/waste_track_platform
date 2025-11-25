package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response;
import lombok.Builder;

@Builder
public record ReportResource(
    String id,
    String citizenId,
    String districtId,
    String latitude,
    String longitude,
    String containerId,
    String reportType,
    String description,
    String status,
    String resolutionNote,
    String resolvedAt,
    String resolvedBy,
    String submittedAt,
    String acknowledgedAt,
    // TODO: Add resources for List<EvidenceResource> evidences
    String createdAt,
    String updatedAt
) {
}