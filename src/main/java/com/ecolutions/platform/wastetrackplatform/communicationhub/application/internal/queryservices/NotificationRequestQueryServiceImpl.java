package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.NotificationRequest;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllNotificationRequestsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetNotificationRequestByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.NotificationRequestQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.NotificationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the NotificationRequestQueryService.
 */
@Service
@RequiredArgsConstructor
public class NotificationRequestQueryServiceImpl implements NotificationRequestQueryService {

    private final NotificationRequestRepository notificationRequestRepository;

    @Override
    public Optional<NotificationRequest> handle(GetNotificationRequestByIdQuery query) {
        try {
            return notificationRequestRepository.findById(query.notificationRequestId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve notification request: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NotificationRequest> handle(GetAllNotificationRequestsQuery query) {
        try {
            return notificationRequestRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve notification requests: " + e.getMessage(), e);
        }
    }
}