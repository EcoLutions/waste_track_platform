package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.PaymentMethod;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeletePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePaymentMethodCommand;

import java.util.Optional;

public interface PaymentMethodCommandService {
    Optional<PaymentMethod> handle(CreatePaymentMethodCommand command);
    Optional<PaymentMethod> handle(UpdatePaymentMethodCommand command);
    Boolean handle(DeletePaymentMethodCommand command);
}