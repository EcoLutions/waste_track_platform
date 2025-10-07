package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects;

public enum ReportType {
    CONTAINER_FULL,
    CONTAINER_DAMAGED,
    GARBAGE_OUTSIDE,
    MISSED_COLLECTION,
    OTHER;

    public static ReportType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Report type cannot be null or blank");
        }
        try {
            return ReportType.valueOf(value.toUpperCase().replace(" ", "_").replace("-", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid report type: " + value);
        }
    }

    public String getDisplayName() {
        return switch (this) {
            case CONTAINER_FULL -> "Container Full";
            case CONTAINER_DAMAGED -> "Container Damaged";
            case GARBAGE_OUTSIDE -> "Garbage Outside Container";
            case MISSED_COLLECTION -> "Missed Collection";
            case OTHER -> "Other";
        };
    }

    public boolean requiresPhoto() {
        return switch (this) {
            case CONTAINER_DAMAGED, GARBAGE_OUTSIDE -> true;
            case CONTAINER_FULL, MISSED_COLLECTION, OTHER -> false;
        };
    }

    public int getPriorityLevel() {
        return switch (this) {
            case CONTAINER_DAMAGED, GARBAGE_OUTSIDE -> 3;
            case CONTAINER_FULL, MISSED_COLLECTION -> 2;
            case OTHER -> 1;
        };
    }
}