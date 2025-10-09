package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands;

public record DeleteContainerCommand(String containerId) {
    public DeleteContainerCommand {
        if (containerId == null || containerId.isBlank()) {
            throw new IllegalArgumentException("Container ID cannot be null or blank");
        }
    }
}