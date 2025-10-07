package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.DeliveryAttempt;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.AttemptStatus;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.ProviderType;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.DeliveryAttemptCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.DeliveryAttemptRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.NotificationRequestId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryAttemptCommandServiceImpl implements DeliveryAttemptCommandService {
    private final DeliveryAttemptRepository deliveryAttemptRepository;

    @Override
    public Optional<DeliveryAttempt> handle(CreateDeliveryAttemptCommand command) {
        try {
            NotificationChannel channel = NotificationChannel.fromString(command.channel());
            ProviderType provider = ProviderType.fromString(command.provider());
            AttemptStatus status = AttemptStatus.fromString(command.status());
            NotificationRequestId requestId = NotificationRequestId.of(command.requestId());
            Money cost = new Money(command.costCurrency(), new BigDecimal(command.costAmount()));
            DeliveryAttempt deliveryAttempt = new DeliveryAttempt(
                requestId,
                channel,
                provider,
                command.attemptNumber()
            );

            deliveryAttempt.setProviderMessageId(command.providerMessageId());
            deliveryAttempt.setStatus(status);
            deliveryAttempt.setCanRetry(command.canRetry());
            deliveryAttempt.setSentAt(LocalDateTime.parse(command.sentAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if (command.deliveredAt() != null && !command.deliveredAt().isBlank()) {
                deliveryAttempt.setDeliveredAt(LocalDateTime.parse(command.deliveredAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            if (command.errorCode() != null) {
                deliveryAttempt.setErrorCode(command.errorCode());
            }
            if (command.errorMessage() != null) {
                deliveryAttempt.setErrorMessage(command.errorMessage());
            }
            deliveryAttempt.setCost(cost);

            var savedDeliveryAttempt = deliveryAttemptRepository.save(deliveryAttempt);
            return Optional.of(savedDeliveryAttempt);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create delivery attempt: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<DeliveryAttempt> handle(UpdateDeliveryAttemptCommand command) {
        try {
            DeliveryAttempt existingDeliveryAttempt = deliveryAttemptRepository.findById(command.deliveryAttemptId())
                .orElseThrow(() -> new IllegalArgumentException("DeliveryAttempt with ID " + command.deliveryAttemptId() + " not found."));

            if (command.channel() != null && !command.channel().isBlank()) {
                existingDeliveryAttempt.setChannel(NotificationChannel.fromString(command.channel()));
            }
            if (command.provider() != null && !command.provider().isBlank()) {
                existingDeliveryAttempt.setProvider(ProviderType.fromString(command.provider()));
            }
            if (command.status() != null && !command.status().isBlank()) {
                existingDeliveryAttempt.setStatus(AttemptStatus.fromString(command.status()));
            }
            if (command.requestId() != null && !command.requestId().isBlank()) {
                existingDeliveryAttempt.setRequestId(NotificationRequestId.of(command.requestId()));
            }
            if (command.providerMessageId() != null && !command.providerMessageId().isBlank()) {
                existingDeliveryAttempt.setProviderMessageId(command.providerMessageId());
            }
            if (command.attemptNumber() != null) {
                existingDeliveryAttempt.setAttemptNumber(command.attemptNumber());
            }
            if (command.canRetry() != null) {
                existingDeliveryAttempt.setCanRetry(command.canRetry());
            }
            if (command.sentAt() != null && !command.sentAt().isBlank()) {
                existingDeliveryAttempt.setSentAt(LocalDateTime.parse(command.sentAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            if (command.deliveredAt() != null && !command.deliveredAt().isBlank()) {
                existingDeliveryAttempt.setDeliveredAt(LocalDateTime.parse(command.deliveredAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
            if (command.errorCode() != null) {
                existingDeliveryAttempt.setErrorCode(command.errorCode());
            }
            if (command.errorMessage() != null) {
                existingDeliveryAttempt.setErrorMessage(command.errorMessage());
            }
            if (command.costAmount() != null && !command.costAmount().isBlank()) {
                existingDeliveryAttempt.setCost(new Money(command.costCurrency(), new BigDecimal(command.costAmount())));
            }

            var updatedDeliveryAttempt = deliveryAttemptRepository.save(existingDeliveryAttempt);
            return Optional.of(updatedDeliveryAttempt);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update delivery attempt: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteDeliveryAttemptCommand command) {
        try {
            DeliveryAttempt existingDeliveryAttempt = deliveryAttemptRepository
                .findById(command.deliveryAttemptId())
                .orElseThrow(() -> new IllegalArgumentException("DeliveryAttempt with ID " + command.deliveryAttemptId() + " not found."));

            deliveryAttemptRepository.delete(existingDeliveryAttempt);
            return true;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete delivery attempt: " + e.getMessage(), e);
        }
    }
}