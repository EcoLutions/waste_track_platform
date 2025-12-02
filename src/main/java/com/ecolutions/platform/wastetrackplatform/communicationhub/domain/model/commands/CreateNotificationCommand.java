package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands;


public record CreateNotificationCommand(String userId, String type, String title, String message, String priority, String channel, String routeId) {
    public CreateNotificationCommand {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or blank");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        if (priority == null || priority.isBlank()) {
            throw new IllegalArgumentException("Priority cannot be null or blank");
        }
        if (channel == null || channel.isBlank()) {
            throw new IllegalArgumentException("Channel cannot be null or blank");
        }
    }
}
