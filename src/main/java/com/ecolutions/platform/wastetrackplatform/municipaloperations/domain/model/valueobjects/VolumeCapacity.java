package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public record VolumeCapacity(BigDecimal cubicMeters) {
    public VolumeCapacity {
        if (cubicMeters == null || cubicMeters.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cubic meters must be greater than zero");
        }
    }
}