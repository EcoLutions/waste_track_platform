package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Distance(BigDecimal kilometers) {
    public Distance {
        if (kilometers == null || kilometers.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Kilometers cannot be null or negative");
        }
    }

    public static String toStringOrNull(Distance distance) {
        return distance == null ? null : distance.kilometers.toString();
    }
}