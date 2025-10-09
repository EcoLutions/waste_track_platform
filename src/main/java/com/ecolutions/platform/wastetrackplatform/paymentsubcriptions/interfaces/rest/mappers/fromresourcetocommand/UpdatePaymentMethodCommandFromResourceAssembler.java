package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdatePaymentMethodResource;

public class UpdatePaymentMethodCommandFromResourceAssembler {
    public static UpdatePaymentMethodCommand toCommandFromResource(String paymentMethodId, UpdatePaymentMethodResource resource) {
        return new UpdatePaymentMethodCommand(
                paymentMethodId,
                resource.type(),
                resource.culqiTokenId(),
                resource.cardBrand(),
                resource.lastFourDigits(),
                resource.expiryMonth(),
                resource.expiryYear()
        );
    }
}