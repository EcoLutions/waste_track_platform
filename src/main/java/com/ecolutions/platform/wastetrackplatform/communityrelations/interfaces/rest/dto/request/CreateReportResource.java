package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateReportResource(
    String citizenId,
    String latitude,
    String longitude,
    String address,
    String districtCode,
    String containerId, // Optional
    String reportType,
    String description,
    List<String> evidenceIds
) {
    public CreateReportResource {
        if (citizenId == null || citizenId.isBlank()) {
            throw new IllegalArgumentException("Citizen ID cannot be null or blank");
        }
        if (latitude == null || latitude.isBlank()) {
            throw new IllegalArgumentException("Latitude cannot be null or blank");
        }
        if (longitude == null || longitude.isBlank()) {
            throw new IllegalArgumentException("Longitude cannot be null or blank");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");
        }
        if (districtCode == null || districtCode.isBlank()) {
            throw new IllegalArgumentException("District code cannot be null or blank");
        }
        if (reportType == null || reportType.isBlank()) {
            throw new IllegalArgumentException("Report type cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}