package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

public record SendUserActivationEmailCommand(String recipientEmail, String username, String activationToken) {
}
