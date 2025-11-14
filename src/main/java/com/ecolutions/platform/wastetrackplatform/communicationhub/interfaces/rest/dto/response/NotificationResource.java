package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response;

import lombok.Builder;

@Builder
public record NotificationResource(
        String id,
        String userId,
        String type,
        String title,
        String message,
        String priority,
        String status,
        String channel,
        String routeId,
        String createdAt,
        String readAt
) {}
