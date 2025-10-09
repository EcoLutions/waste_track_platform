package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum SourceContext {
    CONTAINER_MONITORING,
    ROUTE_PLANNING,
    COMMUNITY_RELATIONS,
    PAYMENT,
    MUNICIPAL_OPS;

    public static SourceContext fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SourceContext cannot be null or blank");
        }
        try {
            return SourceContext.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid SourceContext: " + value);
        }
    }
}