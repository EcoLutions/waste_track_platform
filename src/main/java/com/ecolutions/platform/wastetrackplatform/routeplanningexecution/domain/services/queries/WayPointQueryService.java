package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllWayPointsQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointsByRouteIdQuery;

import java.util.List;
import java.util.Optional;

public interface WayPointQueryService {
    Optional<WayPoint> handle(GetWayPointByIdQuery query);
    List<WayPoint> handle(GetAllWayPointsQuery query);
    List<WayPoint> handle(GetWayPointsByRouteIdQuery query);
}