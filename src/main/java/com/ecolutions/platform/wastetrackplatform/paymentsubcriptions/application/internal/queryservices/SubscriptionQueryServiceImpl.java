package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.Subscription;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetSubscriptionByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllSubscriptionsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.SubscriptionQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Optional<Subscription> handle(GetSubscriptionByIdQuery query) {
        try {
            return subscriptionRepository.findById(query.subscriptionId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptionsQuery query) {
        try {
            return subscriptionRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve subscriptions: " + e.getMessage(), e);
        }
    }
}