package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllWayPointsQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointsByRouteIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.WayPointQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.WayPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WayPointQueryServiceImpl implements WayPointQueryService {
    private final RouteRepository routeRepository;
    private final WayPointRepository wayPointRepository;

    @Override
    public Optional<WayPoint> handle(GetWayPointByIdQuery query) {
        try {
            return wayPointRepository.findById(query.wayPointId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve waypoint: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WayPoint> handle(GetAllWayPointsQuery query) {
        try {
            return wayPointRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve waypoints: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WayPoint> handle(GetWayPointsByRouteIdQuery query) {
        try {
            Route existingRoute = routeRepository.findById(query.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + query.routeId() + " not found."));
            return new ArrayList<>(existingRoute.getWaypoints());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve waypoints by route: " + e.getMessage(), e);
        }
    }
}