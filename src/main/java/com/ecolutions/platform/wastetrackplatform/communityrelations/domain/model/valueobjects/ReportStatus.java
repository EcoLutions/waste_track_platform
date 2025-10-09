package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects;

import java.time.Duration;

public enum ReportStatus {
    SUBMITTED,
    ACKNOWLEDGED,
    IN_PROGRESS,
    RESOLVED,
    REJECTED;

    public static ReportStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Report status cannot be null or blank");
        }
        try {
            return ReportStatus.valueOf(value.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid report status: " + value);
        }
    }

    public String getDisplayName() {
        return switch (this) {
            case SUBMITTED -> "Submitted";
            case ACKNOWLEDGED -> "Acknowledged";
            case IN_PROGRESS -> "In Progress";
            case RESOLVED -> "Resolved";
            case REJECTED -> "Rejected";
        };
    }

    public boolean isActive() {
        return this == SUBMITTED || this == ACKNOWLEDGED || this == IN_PROGRESS;
    }

    public boolean isClosed() {
        return this == RESOLVED || this == REJECTED;
    }

    public boolean canTransitionTo(ReportStatus newStatus) {
        return switch (this) {
            case SUBMITTED -> newStatus == ACKNOWLEDGED || newStatus == REJECTED;
            case ACKNOWLEDGED -> newStatus == IN_PROGRESS || newStatus == REJECTED;
            case IN_PROGRESS -> newStatus == RESOLVED || newStatus == REJECTED;
            case RESOLVED, REJECTED -> false;
        };
    }

    public Duration getMaxResolutionTime() {
        return switch (this) {
            case SUBMITTED, ACKNOWLEDGED -> Duration.ofHours(24);
            case IN_PROGRESS -> Duration.ofHours(72);
            case RESOLVED, REJECTED -> Duration.ZERO;
        };
    }

    public boolean requiresNote() {
        return this == RESOLVED || this == REJECTED;
    }

    public boolean requiresResolver() {
        return this == RESOLVED || this == REJECTED;
    }

    public static String toStringOrNull(ReportStatus reportStatus) {
        return reportStatus != null ? reportStatus.name() : null;
    }
}