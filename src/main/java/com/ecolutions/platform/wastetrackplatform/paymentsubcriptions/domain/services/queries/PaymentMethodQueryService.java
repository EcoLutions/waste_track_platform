package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.PaymentMethod;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllPaymentMethodsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPaymentMethodByIdQuery;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodQueryService {
    Optional<PaymentMethod> handle(GetPaymentMethodByIdQuery query);
    List<PaymentMethod> handle(GetAllPaymentMethodsQuery query);
}