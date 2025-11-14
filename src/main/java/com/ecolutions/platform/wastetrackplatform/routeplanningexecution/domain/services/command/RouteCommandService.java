package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.MarkWayPointAsVisitedCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;

import java.util.Optional;

public interface RouteCommandService {
    Optional<Route> handle(CreateRouteCommand command);
    Optional<Route> handle(UpdateRouteCommand command);
    Boolean handle(DeleteRouteCommand command);
    Optional<Route> handle(MarkWayPointAsVisitedCommand command);
}