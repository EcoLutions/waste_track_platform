package com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.NotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRequestRepository extends JpaRepository<NotificationRequest, String> {
}