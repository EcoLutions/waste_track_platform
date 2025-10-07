package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.NotificationRequest;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllNotificationRequestsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetNotificationRequestByIdQuery;

import java.util.List;
import java.util.Optional;

public interface NotificationRequestQueryService {
    Optional<NotificationRequest> handle(GetNotificationRequestByIdQuery query);
    List<NotificationRequest> handle(GetAllNotificationRequestsQuery query);
}