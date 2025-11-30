package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.*;

import java.util.Optional;

public interface RouteCommandService {
    Optional<Route> handle(CreateRouteCommand command);
    Optional<Route> handle(UpdateRouteCommand command);
    Boolean handle(DeleteRouteCommand command);
    Optional<Route> handle(MarkWayPointAsVisitedCommand command);
    Optional<Route> handle(GenerateOptimizedWaypointsCommand command);
    Optional<Route> handle(UpdateCurrentLocationRouteCommand command);
    Optional<Route> handle(StartRouteCommand command);
    Optional<Route> handle(CompleteRouteCommand command);
    Optional<Route> handle(CancelRouteCommand command);
    Optional<Route> handle(ReOptimizeRouteCommand command);
    Optional<Route> handle(UpdateRouteEstimatesCommand command);
    Optional<Route> handle(ActiveRouteCommand command);
}