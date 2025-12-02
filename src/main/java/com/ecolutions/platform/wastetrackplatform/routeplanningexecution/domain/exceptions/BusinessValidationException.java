package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.exceptions;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
}