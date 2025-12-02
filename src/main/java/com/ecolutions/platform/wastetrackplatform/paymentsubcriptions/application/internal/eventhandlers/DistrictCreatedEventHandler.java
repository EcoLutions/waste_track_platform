package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events.DistrictCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreateSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.SubscriptionCommandService;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("PaymentSubscriptionsDistrictCreatedEventHandler")
public class DistrictCreatedEventHandler {
    private final SubscriptionCommandService subscriptionCommandService;

    public DistrictCreatedEventHandler(SubscriptionCommandService subscriptionCommandService) {
        this.subscriptionCommandService = subscriptionCommandService;
    }

    @EventListener(DistrictCreatedEvent.class)
    @Async
    public void on(DistrictCreatedEvent event) {
        var command = new CreateSubscriptionCommand(DistrictId.of(event.getDistrictId()), PlanId.of(event.getPlanId()));
        subscriptionCommandService.handle(command);
    }
}
