package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

import java.time.LocalDateTime;

public record UpdateRouteCommand(String routeId, LocalDateTime scheduledStartAt) {
    public UpdateRouteCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (scheduledStartAt == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
    }
}