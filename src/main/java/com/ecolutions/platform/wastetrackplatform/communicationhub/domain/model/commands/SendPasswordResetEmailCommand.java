package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;

public record SendPasswordResetEmailCommand(String recipientEmail, String username, String resetToken) {
}