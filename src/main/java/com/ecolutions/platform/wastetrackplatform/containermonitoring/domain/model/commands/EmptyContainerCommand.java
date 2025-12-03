package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record EmptyContainerCommand(String containerId) {
    public EmptyContainerCommand {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}
