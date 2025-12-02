package com.ecolutions.platform.wastetrackplatform.municipaloperations.interfaces.rest.dto.request;

import lombok.Builder;

@Builder
public record UpdateDistrictResource(
    String name,
    String code,
    String depotLatitud,
    String depotLongitude,
    String disposalLatitude,
    String disposalLongitude,
    String operationStartTime,
    String operationEndTime,
    String maxRouteDuration
) {
}