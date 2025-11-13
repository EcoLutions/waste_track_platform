package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects;

public enum CitizenStatus {
    INCOMPLETE,
    COMPLETE;

    public static CitizenStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Citizen status cannot be null or blank");
        }
        try {
            return CitizenStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid citizen status: " + value);
        }
    }

    public boolean isComplete() {
        return this == COMPLETE;
    }

    public boolean isIncomplete() {
        return this == INCOMPLETE;
    }
}
