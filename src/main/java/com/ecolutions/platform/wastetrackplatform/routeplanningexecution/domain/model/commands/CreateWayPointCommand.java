package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record CreateWayPointCommand(
    String routeId,
    String containerId,
    Integer sequenceOrder,
    String priority
) {
    public CreateWayPointCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}