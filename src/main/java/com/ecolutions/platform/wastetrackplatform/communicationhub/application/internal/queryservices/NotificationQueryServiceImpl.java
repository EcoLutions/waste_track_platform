package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.Notification;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetNotificationsByUserIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetUnreadNotificationsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationStatus;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.NotificationQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.NotificationRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationQueryServiceImpl implements NotificationQueryService {
    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> handle(GetNotificationsByUserIdQuery query) {
        log.debug("Getting all notifications for user: {}", query.userId());
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(new UserId(query.userId()));
    }

    @Override
    public List<Notification> handle(GetUnreadNotificationsQuery query) {
        log.debug("Getting unread notifications for user: {}", query.userId());
        return notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(new UserId(query.userId()), NotificationStatus.UNREAD);
    }

    @Override
    public Optional<Notification> findById(String notificationId) {
        log.debug("Finding notification by ID: {}", notificationId);
        return notificationRepository.findById(notificationId);
    }
}
