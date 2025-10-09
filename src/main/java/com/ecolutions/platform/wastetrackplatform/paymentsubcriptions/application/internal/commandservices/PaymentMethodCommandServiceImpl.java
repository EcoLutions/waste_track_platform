package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.PaymentMethod;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeletePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.PaymentMethodType;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.PaymentMethodCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.PaymentMethodRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodCommandServiceImpl implements PaymentMethodCommandService {
    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public Optional<PaymentMethod> handle(CreatePaymentMethodCommand command) {
        try {
            var districtId = new DistrictId(command.districtId());
            var paymentMethodType = PaymentMethodType.fromString(command.type());

            var paymentMethod = new PaymentMethod(
                districtId, paymentMethodType,
                command.culqiTokenId(), command.cardBrand(), command.lastFourDigits(), command.expiryMonth(), command.expiryYear()
            );

            var savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
            return Optional.of(savedPaymentMethod);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create payment method: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PaymentMethod> handle(UpdatePaymentMethodCommand command) {
        var existingPaymentMethod = paymentMethodRepository.findById(command.paymentMethodId())
            .orElseThrow(() -> new IllegalArgumentException("PaymentMethod with ID " + command.paymentMethodId() + " not found."));

        try {
            if (!command.type().isBlank()) {
                var paymentMethodType = PaymentMethodType.fromString(command.type());
                existingPaymentMethod.setType(paymentMethodType);
            }

            if (!command.culqiTokenId().isBlank()) {
                existingPaymentMethod.setCulqiTokenId(command.culqiTokenId());
            }

            if (command.cardBrand() != null && !command.cardBrand().isBlank()) {
                existingPaymentMethod.setCardBrand(command.cardBrand());
            }

            if (command.lastFourDigits() != null && !command.lastFourDigits().isBlank()) {
                existingPaymentMethod.setLastFourDigits(command.lastFourDigits());
            }

            if (command.expiryMonth() != null && command.expiryMonth() >= 1 && command.expiryMonth() <= 12) {
                existingPaymentMethod.setExpiryMonth(command.expiryMonth());
            }

            if (command.expiryYear() != null && command.expiryYear() >= 2024) {
                existingPaymentMethod.setExpiryYear(command.expiryYear());
            }

            var updatedPaymentMethod = paymentMethodRepository.save(existingPaymentMethod);
            return Optional.of(updatedPaymentMethod);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update payment method: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeletePaymentMethodCommand command) {
        var existingPaymentMethod = paymentMethodRepository.findById(command.paymentMethodId())
            .orElseThrow(() -> new IllegalArgumentException("PaymentMethod with ID " + command.paymentMethodId() + " not found."));
        try {
            paymentMethodRepository.delete(existingPaymentMethod);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete payment method: " + e.getMessage(), e);
        }
    }
}