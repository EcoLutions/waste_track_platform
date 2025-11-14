package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request;

/**
 * Resource for updating current location of a route
 */
public record UpdateCurrentLocationResource(
        String latitude,
        String longitude
) {
}
