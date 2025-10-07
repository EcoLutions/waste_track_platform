package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.NotificationRequest;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteNotificationRequestCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateNotificationRequestCommand;

import java.util.Optional;

public interface NotificationRequestCommandService {
    Optional<NotificationRequest> handle(CreateNotificationRequestCommand command);
    Optional<NotificationRequest> handle(UpdateNotificationRequestCommand command);
    Boolean handle(DeleteNotificationRequestCommand command);
}