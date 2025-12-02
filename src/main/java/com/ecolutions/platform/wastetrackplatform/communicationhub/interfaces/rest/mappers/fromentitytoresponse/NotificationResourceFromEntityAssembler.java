package com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.Notification;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationPriority;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationStatus;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationType;
import com.ecolutions.platform.wastetrackplatform.communicationhub.interfaces.rest.dto.response.NotificationResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.RouteId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResourceFromEntity(Notification notification) {
        return NotificationResource.builder()
                .id(notification.getId())
                .userId(UserId.toStringOrNull(notification.getUserId()))
                .type(NotificationType.toStringOrNull(notification.getType()))
                .title(notification.getTitle())
                .message(notification.getMessage())
                .priority(NotificationPriority.toStringOrNull(notification.getPriority()))
                .status(NotificationStatus.toStringOrNull(notification.getStatus()))
                .channel(NotificationChannel.toStringOrNull(notification.getChannel()))
                .routeId(RouteId.toStringOrNull(notification.getRouteId()))
                .createdAt(DateTimeUtils.dateToStringOrNull(notification.getCreatedAt()))
                .readAt(DateTimeUtils.localDateTimeToStringOrNull(notification.getReadAt()))
                .build();
    }
}
