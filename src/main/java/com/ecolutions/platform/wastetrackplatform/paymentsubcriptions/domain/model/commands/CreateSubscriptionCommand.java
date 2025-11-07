package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;

public record CreateSubscriptionCommand(DistrictId districtId, PlanId planId) {
    public CreateSubscriptionCommand {
        if (districtId == null) {
            throw new IllegalArgumentException("District ID cannot be null");
        }
        if (planId == null) {
            throw new IllegalArgumentException("Plan ID cannot be null");
        }
    }
}