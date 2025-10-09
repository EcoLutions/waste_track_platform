package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {
    public static CreateSubscriptionCommand toCommandFromResource(CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(
            resource.districtId(),
            resource.planId(),
            resource.planName(),
            resource.monthlyPriceAmount(),
            resource.monthlyPriceCurrency(),
            resource.startDate(),
            resource.trialEndDate(),
            resource.defaultPaymentMethodId()
        );
    }
}