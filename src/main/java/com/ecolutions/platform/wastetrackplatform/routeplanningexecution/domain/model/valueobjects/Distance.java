package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public record Distance(BigDecimal kilometers, Integer weight) {
    public Distance {
        if (kilometers == null || kilometers.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Kilometers cannot be null or negative");
        }
        if (weight == null || weight < 0) {
            throw new IllegalArgumentException("Weight cannot be null or negative");
        }
    }
}