package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdateSubscriptionResource;

public class UpdateSubscriptionCommandFromResourceAssembler {
    public static UpdateSubscriptionCommand toCommandFromResource(String subscriptionId, UpdateSubscriptionResource resource) {
        return new UpdateSubscriptionCommand(
            subscriptionId,
            resource.planName(),
            resource.monthlyPriceAmount(),
            resource.monthlyPriceCurrency(),
            resource.defaultPaymentMethodId()
        );
    }
}