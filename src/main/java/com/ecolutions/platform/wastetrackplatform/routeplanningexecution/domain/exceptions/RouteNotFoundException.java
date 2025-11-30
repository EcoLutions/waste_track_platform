package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.exceptions;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(String routeId) {
        super("Route with ID " + routeId + " not found");
    }
}
