package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands;

import java.time.LocalDateTime;

/**
 * Command to create a new route.
 * Note: For optimal route optimization with real-time traffic data,
 * scheduledDate should be in the future. Past dates will use fallback algorithm.
 */
public record CreateRouteCommand(
    String districtId,
    String driverId,
    String vehicleId,
    String routeType,
    LocalDateTime scheduledDate
) {
    public CreateRouteCommand {
        if (districtId == null || districtId.isBlank()) {
            throw new IllegalArgumentException("District ID cannot be null or blank");
        }
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver ID cannot be null or blank");
        }
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or blank");
        }
        if (routeType == null || routeType.isBlank()) {
            throw new IllegalArgumentException("Route type cannot be null or blank");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null");
        }
        // Note: We allow past dates for testing/demo purposes, but log a warning
        // The optimization service will handle this gracefully by using fallback algorithm
    }
}