package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.DeliveryAttempt;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.CreateDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.UpdateDeliveryAttemptCommand;
import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.commands.DeleteDeliveryAttemptCommand;

import java.util.Optional;

public interface DeliveryAttemptCommandService {
    Optional<DeliveryAttempt> handle(CreateDeliveryAttemptCommand command);
    Optional<DeliveryAttempt> handle(UpdateDeliveryAttemptCommand command);
    Boolean handle(DeleteDeliveryAttemptCommand command);
}