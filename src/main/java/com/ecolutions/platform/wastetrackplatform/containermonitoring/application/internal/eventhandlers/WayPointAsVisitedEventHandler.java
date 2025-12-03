package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.EmptyContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.ContainerCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events.WayPointAsVisitedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service("WayPointAsVisitedEventHandler")
public class WayPointAsVisitedEventHandler {
    private final ContainerCommandService containerCommandService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WayPointAsVisitedEventHandler.class);

    public WayPointAsVisitedEventHandler(ContainerCommandService containerCommandService) {
        this.containerCommandService = containerCommandService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void on(WayPointAsVisitedEvent event) {
        LOGGER.info("Handling WayPointAsVisitedEvent for waypoint: {} associated with container: {}", event.getWayPointId(), event.getContainerId());
        var command = new EmptyContainerCommand(event.getContainerId());
        containerCommandService.handle(command);
    }
}
