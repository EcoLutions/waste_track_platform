package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.NotificationRequest;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.NotificationRequestCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.NotificationRequestRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationRequestCommandServiceImpl implements NotificationRequestCommandService {

    private final NotificationRequestRepository notificationRequestRepository;

    @Override
    public Optional<NotificationRequest> handle(CreateNotificationRequestCommand command) {
        try {
            SourceContext sourceContext = SourceContext.fromString(command.sourceContext());
            RecipientType recipientType = RecipientType.fromString(command.recipientType());
            MessageType messageType = MessageType.fromString(command.messageType());
            NotificationPriority priority = NotificationPriority.fromString(command.priority());

            List<NotificationChannel> channels = new ArrayList<>();
            for (String channelStr : command.channels()) {
                channels.add(NotificationChannel.fromString(channelStr));
            }
            RecipientId recipientId = RecipientId.of(command.recipientId());
            EmailAddress recipientEmail = new EmailAddress(command.recipientEmail());
            PhoneNumber recipientPhone = new PhoneNumber(command.recipientPhone());
            TemplateId templateId = TemplateId.of(command.templateId());

            NotificationRequest notificationRequest = new NotificationRequest(
                sourceContext,
                recipientId,
                recipientType,
                recipientEmail,
                recipientPhone,
                messageType,
                templateId,
                command.templateData(),
                channels,
                priority,
                command.scheduledFor(),
                command.expiresAt()
            );

            var savedNotificationRequest = notificationRequestRepository.save(notificationRequest);
            return Optional.of(savedNotificationRequest);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create notification request: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<NotificationRequest> handle(UpdateNotificationRequestCommand command) {
        try {
            NotificationRequest existingNotificationRequest = notificationRequestRepository
                .findById(command.notificationRequestId())
                .orElseThrow(() -> new IllegalArgumentException("NotificationRequest with ID " + command.notificationRequestId() + " not found."));

            if (command.sourceContext() != null) {
                existingNotificationRequest.setSourceContext(SourceContext.fromString(command.sourceContext()));
            }

            if (command.recipientId() != null) {
                existingNotificationRequest.setRecipientId(RecipientId.of(command.recipientId()));
            }

            if (command.recipientType() != null) {
                existingNotificationRequest.setRecipientType(RecipientType.fromString(command.recipientType()));
            }

            if (command.recipientEmail() != null) {
                existingNotificationRequest.setRecipientEmail(new EmailAddress(command.recipientEmail()));
            }

            if (command.recipientPhone() != null) {
                existingNotificationRequest.setRecipientPhone(new PhoneNumber(command.recipientPhone()));
            }

            if (command.messageType() != null) {
                existingNotificationRequest.setMessageType(MessageType.fromString(command.messageType()));
            }

            if (command.templateId() != null) {
                existingNotificationRequest.setTemplateId(TemplateId.of(command.templateId()));
            }

            if (command.templateData() != null) {
                existingNotificationRequest.setTemplateData(command.templateData());
            }

            if (command.channels() != null) {
                existingNotificationRequest.setChannels(new ArrayList<>());
                for (String channelStr : command.channels()) {
                    existingNotificationRequest.addChannel(NotificationChannel.fromString(channelStr));
                }
            }

            if (command.priority() != null) {
                existingNotificationRequest.setPriority(NotificationPriority.fromString(command.priority()));
            }

            if (command.scheduledFor() != null) {
                existingNotificationRequest.setScheduledFor(command.scheduledFor());
            }

            if (command.expiresAt() != null) {
                existingNotificationRequest.setExpiresAt(command.expiresAt());
            }

            var updatedNotificationRequest = notificationRequestRepository.save(existingNotificationRequest);
            return Optional.of(updatedNotificationRequest);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update notification request: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteNotificationRequestCommand command) {
        try {
            NotificationRequest existingNotificationRequest = notificationRequestRepository
                .findById(command.notificationRequestId())
                .orElseThrow(() -> new IllegalArgumentException("NotificationRequest with ID " + command.notificationRequestId() + " not found."));

            notificationRequestRepository.delete(existingNotificationRequest);
            return true;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete notification request: " + e.getMessage(), e);
        }
    }
}