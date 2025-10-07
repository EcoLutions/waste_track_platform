package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum RequestStatus {
    PENDING,
    SENT,
    FAILED,
    EXPIRED;

    public static RequestStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RequestStatus cannot be null or blank");
        }
        try {
            return RequestStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid RequestStatus: " + value);
        }
    }
}