package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands;

public record DeleteCitizenCommand(String citizenId) {
    public DeleteCitizenCommand {
        if (citizenId == null || citizenId.isBlank()) {
            throw new IllegalArgumentException("Citizen ID cannot be null or blank");
        }
    }
}