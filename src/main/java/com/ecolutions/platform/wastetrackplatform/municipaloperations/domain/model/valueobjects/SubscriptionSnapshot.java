package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.SubscriptionId;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record SubscriptionSnapshot(
        SubscriptionId subscriptionId,
        String planName,
        Integer maxVehicles,
        Integer maxDrivers,
        Integer maxContainers,
        LocalDate startedAt,
        LocalDate endedAt
) {
    public SubscriptionSnapshot {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null");
        }
        if (planName == null || planName.isBlank()) {
            throw new IllegalArgumentException("Plan name cannot be null or blank");
        }
        if (maxVehicles == null || maxVehicles < 0) {
            throw new IllegalArgumentException("Max vehicles cannot be null or negative");
        }
        if (maxDrivers == null || maxDrivers < 0) {
            throw new IllegalArgumentException("Max drivers cannot be null or negative");
        }
        if (maxContainers == null || maxContainers < 0) {
            throw new IllegalArgumentException("Max containers cannot be null or negative");
        }
        if (startedAt == null) {
            throw new IllegalArgumentException("Started at cannot be null");
        }
        if (endedAt == null) {
            throw new IllegalArgumentException("Ended at cannot be null");
        } else if (endedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Ended at cannot be before started at");
        }
    }
}
