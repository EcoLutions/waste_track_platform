package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.exceptions;

public class VehicleScheduleConflictException extends RuntimeException {
    public VehicleScheduleConflictException(String message) {
        super(message);
    }
}