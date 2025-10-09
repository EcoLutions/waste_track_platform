package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

public record DeleteDeliveryAttemptCommand(String deliveryAttemptId) {
    public DeleteDeliveryAttemptCommand {
        if (deliveryAttemptId == null || deliveryAttemptId.isBlank()) {
            throw new IllegalArgumentException("Delivery Attempt ID cannot be null or blank");
        }
    }
}