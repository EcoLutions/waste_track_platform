package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetRouteByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.RouteQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
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
}