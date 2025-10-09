package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands;

public record DeleteReportCommand(String reportId) {
    public DeleteReportCommand {
        if (reportId == null || reportId.isBlank()) {
            throw new IllegalArgumentException("Report ID cannot be null or blank");
        }
    }
}