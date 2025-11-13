package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands;

public record UpdateReportCommand(
    String reportId,
    String latitude,
    String longitude,
    String containerId,
    String reportType,
    String description,
    String resolutionNote
) {
    public UpdateReportCommand {
        if (reportId == null || reportId.isBlank()) {
            throw new IllegalArgumentException("Report ID cannot be null or blank");
        }
        if (latitude == null || latitude.isBlank()) {
            throw new IllegalArgumentException("Latitude cannot be null or blank");
        }
        if (longitude == null || longitude.isBlank()) {
            throw new IllegalArgumentException("Longitude cannot be null or blank");
        }
        if (reportType == null || reportType.isBlank()) {
            throw new IllegalArgumentException("Report type cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}