package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

public record PlanId(String planId) {
    public PlanId {
        if (planId == null || planId.isBlank()) {
            throw new IllegalArgumentException("Plan ID cannot be null or blank");
        }
    }

    public static PlanId of(String planId) {
        if (planId == null || planId.isBlank()) {
            return null;
        }
        return new PlanId(planId);
    }

    public static String toStringOrNull(PlanId planId) {
        return planId != null ? planId.planId() : null;
    }
}