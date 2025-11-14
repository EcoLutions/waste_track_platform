package com.ecolutions.platform.wastetrackplatform.communicationhub.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.Notification;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.MarkNotificationAsReadCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command.NotificationCommandService;
import com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;

    @Override
    public Optional<Notification> handle(CreateNotificationCommand command) {
        log.info("Creating notification for user: {}, type: {}", command.userId(), command.type());
        Notification notification = new Notification(command);
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created with ID: {}", savedNotification.getId());
        return Optional.of(savedNotification);
    }

    @Override
    public Optional<Notification> handle(MarkNotificationAsReadCommand command) {
        log.info("Marking notification as read: {}", command.notificationId());
        Notification notification = notificationRepository.findById(command.notificationId())
                .orElseThrow(() -> new IllegalArgumentException("Notification with ID " + command.notificationId() + " not found."));
        notification.markAsRead();
        Notification updatedNotification = notificationRepository.save(notification);
        log.info("Notification {} marked as read", command.notificationId());
        return Optional.of(updatedNotification);
    }
}
