package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

public record MarkWayPointAsVisitedCommand(String routeId, String waypointId) {
    public MarkWayPointAsVisitedCommand {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Route ID cannot be null or blank");
        }
        if (waypointId == null || waypointId.isBlank()) {
            throw new IllegalArgumentException("Waypoint ID cannot be null or blank");
        }
    }
}
