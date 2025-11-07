package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.outboundservices.acl;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.PlanSnapshot;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.contexts.PaymentSubscriptionsContextFacade;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.acl.dtos.PlanInfoDTO;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.BillingPeriod;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("MunicipalOperationsExternalPaymentSubscriptionsService")
public class ExternalPaymentSubscriptionsService {
    private final PaymentSubscriptionsContextFacade paymentSubscriptionsContextFacade;

    public ExternalPaymentSubscriptionsService(PaymentSubscriptionsContextFacade paymentSubscriptionsContextFacade) {
        this.paymentSubscriptionsContextFacade = paymentSubscriptionsContextFacade;
    }

    public Optional<PlanSnapshot> getPlanInfo(String planId) {
        return paymentSubscriptionsContextFacade.getPlanInfo(planId)
                .map(this::toPlanSnapshot);
    }

    private PlanSnapshot toPlanSnapshot(PlanInfoDTO dto) {
        return PlanSnapshot.from(
                PlanId.of(dto.planId()),
                dto.planName(),
                dto.maxVehicles(),
                dto.maxDrivers(),
                dto.maxContainers(),
                Money.of(dto.currency(), dto.priceAmount()),
                BillingPeriod.fromString(dto.billingPeriod())
        );
    }
}
