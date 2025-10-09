package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.Subscription;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeleteSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.SubscriptionStatus;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.SubscriptionCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PaymentMethodId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        try {
            var districtId = new DistrictId(command.districtId());
            var planId = new PlanId(command.planId());
            var money = new Money(command.monthlyPriceCurrency(), new BigDecimal(command.monthlyPriceAmount()));
            var startDate = LocalDate.parse(command.startDate());
            var trialEndDate = LocalDate.parse(command.trialEndDate());
            var paymentMethodId = PaymentMethodId.of(command.defaultPaymentMethodId());

            var subscription = new Subscription(districtId, planId, command.planName(), money, startDate, trialEndDate, paymentMethodId);

            var savedSubscription = subscriptionRepository.save(subscription);
            return Optional.of(savedSubscription);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Subscription> handle(UpdateSubscriptionCommand command) {
        try {
            var existingSubscription = subscriptionRepository.findById(command.subscriptionId())
                    .orElseThrow(() -> new IllegalArgumentException("Subscription with ID " + command.subscriptionId() + " not found."));

            if (existingSubscription.getStatus() != SubscriptionStatus.ACTIVE) {
                throw new IllegalStateException("Only active subscriptions can be updated.");
            }

            if (command.planName() != null && !command.planName().isBlank()) {
                existingSubscription.setPlanName(command.planName());
            }

            if (command.monthlyPriceAmount() != null && !command.monthlyPriceAmount().isBlank() && command.monthlyPriceCurrency() != null && !command.monthlyPriceCurrency().isBlank()) {
                existingSubscription.setMonthlyPrice(new Money(command.monthlyPriceCurrency(), new BigDecimal(command.monthlyPriceAmount())));
            }

            if (command.defaultPaymentMethodId() != null) {
                existingSubscription.setDefaultPaymentMethodId(new PaymentMethodId(command.defaultPaymentMethodId()));
            }

            var updatedSubscription = subscriptionRepository.save(existingSubscription);
            return Optional.of(updatedSubscription);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteSubscriptionCommand command) {
        try {
            var existingSubscription = subscriptionRepository.findById(command.subscriptionId())
                    .orElseThrow(() -> new IllegalArgumentException("Subscription with ID " + command.subscriptionId() + " not found."));
            subscriptionRepository.delete(existingSubscription);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete subscription: " + e.getMessage(), e);
        }
    }
}