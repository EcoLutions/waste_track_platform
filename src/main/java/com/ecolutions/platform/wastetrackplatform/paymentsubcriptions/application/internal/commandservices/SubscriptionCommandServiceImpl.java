package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.Subscription;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.SubscriptionCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.PlanCatalogRepository;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanCatalogRepository planCatalogRepository;

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        var plan = planCatalogRepository.findById(command.planId().planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan with id " + command.planId().planId() + " does not exist."));

        var subscription = new Subscription(command, plan.getPrice(), plan.getBillingPeriod());
        var savedSubscription = subscriptionRepository.save(subscription);
        return Optional.of(savedSubscription);
    }
}