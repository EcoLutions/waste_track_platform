package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Priority(PriorityLevel level) {
    public Priority() {
        this(PriorityLevel.LOW);
    }
    public Priority(PriorityLevel level) {
        this.level = level == null ? PriorityLevel.LOW : level;
    }

    public static String toStringOrNull(Priority priority) {
        return priority != null ? priority.level().name() : null;
    }
}