package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetRouteByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetActiveRoutesByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.RouteQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteQueryServiceImpl implements RouteQueryService {
    private final RouteRepository routeRepository;

    @Override
    public Optional<Route> handle(GetRouteByIdQuery query) {
        try {
            return routeRepository.findById(query.routeId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve route: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Route> handle(GetAllRoutesQuery query) {
        try {
            return routeRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve routes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Route> handle(GetActiveRoutesByDistrictIdQuery query) {
        try {
            var districtId = new DistrictId(query.districtId());
            var activeStatuses = List.of(RouteStatus.ASSIGNED, RouteStatus.IN_PROGRESS);
            return routeRepository.findActiveRoutesByDistrictId(districtId, activeStatuses);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve active routes by district ID: " + e.getMessage(), e);
        }
    }
}