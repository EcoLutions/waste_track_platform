package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.exceptions;

public class DriverScheduleConflictException extends RuntimeException {
    public DriverScheduleConflictException(String message) {
        super(message);
    }
}