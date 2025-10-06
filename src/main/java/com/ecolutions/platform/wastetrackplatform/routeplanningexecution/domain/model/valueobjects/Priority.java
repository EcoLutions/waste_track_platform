package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Priority(PriorityLevel level) {
    public Priority {
        if (level == null) {
            throw new IllegalArgumentException("Priority level cannot be null");
        }
    }
}