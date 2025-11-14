package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.scheduledservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update.RouteUpdateService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteUpdateScheduler {
    private final RouteRepository routeRepository;
    private final RouteUpdateService routeUpdateService;

    @Scheduled(fixedDelay = 600000) // 10 minutes in milliseconds
    public void updateActiveRoutes() {
        log.debug("Starting scheduled route updates");

        List<Route> activeRoutes = routeRepository.findByStatus(RouteStatus.IN_PROGRESS);

        if (activeRoutes.isEmpty()) {
            log.debug("No active routes to update");
            return;
        }

        log.info("Updating {} active routes", activeRoutes.size());

        for (Route route : activeRoutes) {
            try {
                if (routeUpdateService.shouldUpdate(route)) {
                    routeUpdateService.updateRouteEstimates(route);
                    log.debug("Updated route {}", route.getId());
                }
            } catch (Exception e) {
                log.error("Error updating route {}: {}", route.getId(), e.getMessage(), e);
            }
        }

        log.info("Completed scheduled route updates");
    }
}
