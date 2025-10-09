package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.PaymentMethod;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllPaymentMethodsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPaymentMethodByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.PaymentMethodQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodQueryServiceImpl implements PaymentMethodQueryService {
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public Optional<PaymentMethod> handle(GetPaymentMethodByIdQuery query) {
        try {
            return paymentMethodRepository.findById(query.paymentMethodId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve payment method: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentMethod> handle(GetAllPaymentMethodsQuery query) {
        try {
            return paymentMethodRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve payment methods: " + e.getMessage(), e);
        }
    }
}