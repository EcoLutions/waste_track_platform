package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.MessageTemplate;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateMessageTemplateCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.NotificationChannel;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects.TemplateCategory;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.MessageTemplateCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.MessageTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageTemplateCommandServiceImpl implements MessageTemplateCommandService {
    private final MessageTemplateRepository messageTemplateRepository;

    @Override
    public Optional<MessageTemplate> handle(CreateMessageTemplateCommand command) {
        try {
            TemplateCategory category = TemplateCategory.fromString(command.category());
            List<NotificationChannel> channels = new ArrayList<>();
            for (String channelStr : command.supportedChannels()) {
                channels.add(NotificationChannel.fromString(channelStr));
            }
            MessageTemplate messageTemplate = new MessageTemplate(
                command.name(),
                category,
                channels
            );

            messageTemplate.setEmailSubject(command.emailSubject());
            messageTemplate.setEmailBody(command.emailBody());
            messageTemplate.setSmsBody(command.smsBody());
            messageTemplate.setPushTitle(command.pushTitle());
            messageTemplate.setPushBody(command.pushBody());

            if (command.variables() != null) {
                for (String variable : command.variables()) {
                    messageTemplate.addVariable(variable);
                }
            }

            messageTemplate.setIsActive(command.isActive());

            var savedMessageTemplate = messageTemplateRepository.save(messageTemplate);
            return Optional.of(savedMessageTemplate);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create message template: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<MessageTemplate> handle(UpdateMessageTemplateCommand command) {
        try {
            MessageTemplate existingMessageTemplate = messageTemplateRepository
                .findById(command.messageTemplateId())
                .orElseThrow(() -> new IllegalArgumentException("MessageTemplate with ID " + command.messageTemplateId() + " not found."));

            if (command.name() != null) {
                existingMessageTemplate.setName(command.name());
            }
            if (command.category() != null) {
                TemplateCategory category = TemplateCategory.fromString(command.category());
                existingMessageTemplate.setCategory(category);
            }
            if (command.supportedChannels() != null) {
                List<NotificationChannel> channels = new ArrayList<>();
                for (String channelStr : command.supportedChannels()) {
                    channels.add(NotificationChannel.fromString(channelStr));
                }
                existingMessageTemplate.setSupportedChannels(channels);
            }
            if (command.emailSubject() != null) {
                existingMessageTemplate.setEmailSubject(command.emailSubject());
            }
            if (command.emailBody() != null) {
                existingMessageTemplate.setEmailBody(command.emailBody());
            }
            if (command.smsBody() != null) {
                existingMessageTemplate.setSmsBody(command.smsBody());
            }
            if (command.pushTitle() != null) {
                existingMessageTemplate.setPushTitle(command.pushTitle());
            }
            if (command.pushBody() != null) {
                existingMessageTemplate.setPushBody(command.pushBody());
            }

            existingMessageTemplate.getVariables().clear();

            if (command.variables() != null) {
                for (String variable : command.variables()) {
                    existingMessageTemplate.addVariable(variable);
                }
            }

            existingMessageTemplate.setIsActive(command.isActive());

            var updatedMessageTemplate = messageTemplateRepository.save(existingMessageTemplate);
            return Optional.of(updatedMessageTemplate);

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update message template: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteMessageTemplateCommand command) {
        try {
            MessageTemplate existingMessageTemplate = messageTemplateRepository
                .findById(command.messageTemplateId())
                .orElseThrow(() -> new IllegalArgumentException("MessageTemplate with ID " + command.messageTemplateId() + " not found."));

            messageTemplateRepository.delete(existingMessageTemplate);
            return true;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete message template: " + e.getMessage(), e);
        }
    }
}