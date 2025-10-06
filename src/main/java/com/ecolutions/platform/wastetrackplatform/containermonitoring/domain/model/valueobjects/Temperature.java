package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public record Temperature(BigDecimal celsius) {
    public Temperature {
        if (celsius == null) {
            throw new IllegalArgumentException("Celsius cannot be null");
        }
        if (celsius.compareTo(BigDecimal.valueOf(-50)) < 0 || celsius.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Temperature must be between -50°C and 100°C");
        }
    }
}