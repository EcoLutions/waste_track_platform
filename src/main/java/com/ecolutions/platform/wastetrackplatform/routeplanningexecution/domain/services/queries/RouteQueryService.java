package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetRouteByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;

import java.util.List;
import java.util.Optional;

public interface RouteQueryService {
    Optional<Route> handle(GetRouteByIdQuery query);
    List<Route> handle(GetAllRoutesQuery query);
}