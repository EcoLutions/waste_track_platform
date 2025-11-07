package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.contexts;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.dtos.PlanInfoDTO;

import java.util.Optional;

public interface PaymentSubscriptionsContextFacade {
    Optional<PlanInfoDTO> getPlanInfo(String planId);
}
