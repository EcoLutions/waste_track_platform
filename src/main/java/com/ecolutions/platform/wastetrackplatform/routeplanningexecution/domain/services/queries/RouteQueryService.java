package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.*;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;

import java.util.List;
import java.util.Optional;

public interface RouteQueryService {
    Optional<Route> handle(GetRouteByIdQuery query);
    List<Route> handle(GetAllRoutesQuery query);
    List<Route> handle(GetActiveRoutesByDistrictIdQuery query);
    List<Route> handle(GetAllPlannedRoutesQuery query);
    List<Route> handle(GetAllInProgressRoutesQuery query);
}