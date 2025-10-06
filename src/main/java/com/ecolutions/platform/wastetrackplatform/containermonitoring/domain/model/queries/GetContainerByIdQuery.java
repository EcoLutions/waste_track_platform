package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries;

public record GetContainerByIdQuery(String containerId) {
    public GetContainerByIdQuery {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}