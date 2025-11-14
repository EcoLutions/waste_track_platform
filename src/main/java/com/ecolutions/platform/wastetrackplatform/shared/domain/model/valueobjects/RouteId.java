package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record RouteId(String value) {
    public RouteId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RouteId cannot be null or empty");
        }
    }

    public static RouteId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new RouteId(value);
    }

    public static String toStringOrNull(RouteId routeId) {
        return routeId != null ? routeId.value() : null;
    }
}
