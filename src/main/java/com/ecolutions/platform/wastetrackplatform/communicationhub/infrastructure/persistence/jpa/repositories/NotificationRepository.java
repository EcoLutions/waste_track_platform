package com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.Notification;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.RouteId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(UserId userId);
    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(UserId userId, NotificationStatus status);
    List<Notification> findByRouteIdOrderByCreatedAtDesc(RouteId routeId);
}
