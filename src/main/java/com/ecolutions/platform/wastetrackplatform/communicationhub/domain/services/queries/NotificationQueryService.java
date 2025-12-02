package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.Notification;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetNotificationsByUserIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetUnreadNotificationsQuery;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {
    List<Notification> handle(GetNotificationsByUserIdQuery query);
    List<Notification> handle(GetUnreadNotificationsQuery query);
    Optional<Notification> findById(String notificationId);
}
