package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CollectionFrequency(Integer days) {
    public CollectionFrequency {
        if (days == null || days <= 0) {
            throw new IllegalArgumentException("Days cannot be null or less than or equal to zero");
        }
    }

    public static Integer toIntegerOrNull(CollectionFrequency collectionFrequency) {
        return collectionFrequency != null ? collectionFrequency.days : null;
    }
}