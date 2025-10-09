package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.Subscription;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeleteSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdateSubscriptionCommand;

import java.util.Optional;

public interface SubscriptionCommandService {
    Optional<Subscription> handle(CreateSubscriptionCommand command);
    Optional<Subscription> handle(UpdateSubscriptionCommand command);
    Boolean handle(DeleteSubscriptionCommand command);
}