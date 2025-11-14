package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.Notification;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.MarkNotificationAsReadCommand;

import java.util.Optional;

public interface NotificationCommandService {
    Optional<Notification> handle(CreateNotificationCommand command);
    Optional<Notification> handle(MarkNotificationAsReadCommand command);
}
