package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries;

public record GetDeliveryAttemptByIdQuery(String deliveryAttemptId) {
    public GetDeliveryAttemptByIdQuery {
        if (deliveryAttemptId == null || deliveryAttemptId.isBlank()) {
            throw new IllegalArgumentException("Delivery Attempt ID cannot be null or blank");
        }
    }
}