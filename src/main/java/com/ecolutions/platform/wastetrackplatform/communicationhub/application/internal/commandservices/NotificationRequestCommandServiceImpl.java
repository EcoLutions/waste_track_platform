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
            // Convert string enums to enum objects
            SourceContext sourceContext = SourceContext.fromString(command.sourceContext());
            RecipientType recipientType = RecipientType.fromString(command.recipientType());
            MessageType messageType = MessageType.fromString(command.messageType());
            NotificationPriority priority = NotificationPriority.fromString(command.priority());

            // Convert string channels to enum objects
            List<NotificationChannel> channels = new ArrayList<>();
            for (String channelStr : command.channels()) {
                channels.add(NotificationChannel.fromString(channelStr));
            }

            // Create value objects
            RecipientId recipientId = RecipientId.of(command.recipientId());
            EmailAddress recipientEmail = new EmailAddress(command.recipientEmail());
            PhoneNumber recipientPhone = new PhoneNumber(command.recipientPhone());
            TemplateId templateId = TemplateId.of(command.templateId());

            // Create the notification request
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

            // Convert string enums to enum objects
            SourceContext sourceContext = SourceContext.fromString(command.sourceContext());
            RecipientType recipientType = RecipientType.fromString(command.recipientType());
            MessageType messageType = MessageType.fromString(command.messageType());
            NotificationPriority priority = NotificationPriority.fromString(command.priority());

            // Convert string channels to enum objects
            List<NotificationChannel> channels = new ArrayList<>();
            for (String channelStr : command.channels()) {
                channels.add(NotificationChannel.fromString(channelStr));
            }

            // Update fields
            existingNotificationRequest.setSourceContext(sourceContext);
            existingNotificationRequest.setRecipientId(RecipientId.of(command.recipientId()));
            existingNotificationRequest.setRecipientType(recipientType);
            existingNotificationRequest.setRecipientEmail(new EmailAddress(command.recipientEmail()));
            existingNotificationRequest.setRecipientPhone(new PhoneNumber(command.recipientPhone()));
            existingNotificationRequest.setMessageType(messageType);
            existingNotificationRequest.setTemplateId(TemplateId.of(command.templateId()));
            existingNotificationRequest.setTemplateData(command.templateData());
            existingNotificationRequest.setChannels(channels);
            existingNotificationRequest.setPriority(priority);
            existingNotificationRequest.setScheduledFor(command.scheduledFor());
            existingNotificationRequest.setExpiresAt(command.expiresAt());

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