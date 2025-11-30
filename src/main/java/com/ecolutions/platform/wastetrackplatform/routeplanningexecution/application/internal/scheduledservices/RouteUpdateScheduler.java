package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.scheduledservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.update.RouteUpdateService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.ActiveRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllInProgressRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllPlannedRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.RouteCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.RouteQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteUpdateScheduler {
    private final RouteUpdateService routeUpdateService;
    private final RouteQueryService routeQueryService;
    private final RouteCommandService routeCommandService;

    @Scheduled(fixedDelay = 600000) // 10 minutes in milliseconds
    public void updateInProgressRoutes() {
        log.debug("Starting scheduled route updates");

        List<Route> inProgressRoutes = routeQueryService.handle(new GetAllInProgressRoutesQuery());

        if (inProgressRoutes.isEmpty()) {
            log.debug("No in progress routes to update");
            return;
        }

        log.info("Updating {} in progress routes", inProgressRoutes.size());

        for (Route route : inProgressRoutes) {
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

    @Scheduled(fixedRate = 60000) // 1 minute in milliseconds
    public void activatePlannedRoutes() {

        log.info("Checking planned routes for auto-activation...");
        List<Route> plannedRoutes = routeQueryService.handle(new GetAllPlannedRoutesQuery());
        LocalDateTime now = LocalDateTime.now();

        for (Route route : plannedRoutes) {
            if (shouldActivate(route, now)) {
                routeCommandService.handle(new ActiveRouteCommand(route.getId()));
            }
        }
    }

    private boolean shouldActivate(Route route, LocalDateTime now) {
        return now.isAfter(route.getScheduledStartAt().minusMinutes(30));
    }
}
