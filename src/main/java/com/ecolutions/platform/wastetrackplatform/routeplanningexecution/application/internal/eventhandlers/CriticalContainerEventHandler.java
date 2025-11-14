package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events.ContainerBecameCriticalEvent;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.insertion.DynamicWaypointInsertionService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CriticalContainerEventHandler {
    private final RouteRepository routeRepository;
    private final DynamicWaypointInsertionService dynamicInsertionService;

    @EventListener
    @Async
    public void handleContainerBecameCritical(ContainerBecameCriticalEvent event) {
        log.info("Container {} became CRITICAL (fill level: {}%)", event.getContainerId(), event.getContainerId());

        // Find active route in district
        Optional<Route> activeRoute = routeRepository
                .findActiveRoutesByDistrictId(new DistrictId(event.getContainerId()), List.of(RouteStatus.IN_PROGRESS))
                .stream()
                .filter(route -> route.getStatus() == RouteStatus.IN_PROGRESS)
                .findFirst();

        if (activeRoute.isEmpty()) {
            log.info("No active route found for district {}, container will be included in next route planning", event.getContainerId());
            return;
        }

        Route route = activeRoute.get();

        // Check if the container is already in route
        boolean alreadyIncluded = route.getWaypoints().stream()
                .anyMatch(wp -> wp.getContainerId().value().equals(event.getContainerId()));

        if (alreadyIncluded) {
            log.info("Container {} is already in route {}", event.getContainerId(), route.getId());
            return;
        }

        // Attempt dynamic insertion
        log.info("Attempting to dynamically insert container {} into route {}", event.getContainerId(), route.getId());
        dynamicInsertionService.attemptInsertCriticalContainer(route, event);
    }
}
