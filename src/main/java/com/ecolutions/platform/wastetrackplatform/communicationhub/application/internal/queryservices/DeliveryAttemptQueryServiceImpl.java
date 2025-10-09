package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.DeliveryAttempt;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetDeliveryAttemptByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.queries.GetAllDeliveryAttemptsQuery;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.queries.DeliveryAttemptQueryService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.DeliveryAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryAttemptQueryServiceImpl implements DeliveryAttemptQueryService {
    private final DeliveryAttemptRepository deliveryAttemptRepository;

    @Override
    public Optional<DeliveryAttempt> handle(GetDeliveryAttemptByIdQuery query) {
        try {
            return deliveryAttemptRepository.findById(query.deliveryAttemptId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve delivery attempt: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DeliveryAttempt> handle(GetAllDeliveryAttemptsQuery query) {
        try {
            return deliveryAttemptRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve delivery attempts: " + e.getMessage(), e);
        }
    }
}