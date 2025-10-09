package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreatePaymentMethodResource;

public class CreatePaymentMethodCommandFromResourceAssembler {
    public static CreatePaymentMethodCommand toCommandFromResource(CreatePaymentMethodResource resource) {
        return new CreatePaymentMethodCommand(
            resource.districtId(),
            resource.type(),
            resource.culqiTokenId(),
            resource.cardBrand(),
            resource.lastFourDigits(),
            resource.expiryMonth(),
            resource.expiryYear()
        );
    }
}