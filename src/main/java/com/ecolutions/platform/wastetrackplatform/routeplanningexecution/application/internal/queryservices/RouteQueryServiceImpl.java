package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetRouteByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetActiveRoutesByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.RouteQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories.RouteRepository;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.specifications.RouteSpecifications;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteQueryServiceImpl implements RouteQueryService {
    private final RouteRepository routeRepository;

    @Override
    public Optional<Route> handle(GetRouteByIdQuery query) {
        return routeRepository.findById(query.routeId());
    }

    @Override
    public List<Route> handle(GetAllRoutesQuery query) {
        List<RouteStatus> statusList = query.statuses() != null
                ? query.statuses().stream()
                .map(RouteStatus::fromString)
                .toList()
                : Collections.emptyList();

        RouteStatus singleStatus = query.status() != null
                ? RouteStatus.fromString(query.status())
                : null;

        Specification<Route> spec = RouteSpecifications.withFilters(
                query.districtId(),
                query.driverId(),
                query.vehicleId(),
                singleStatus,
                statusList
        );

        return routeRepository.findAll(spec);
    }

    @Override
    public List<Route> handle(GetActiveRoutesByDistrictIdQuery query) {
        var districtId = new DistrictId(query.districtId());
        var activeStatuses = List.of(RouteStatus.ACTIVE, RouteStatus.IN_PROGRESS);
        return routeRepository.findActiveRoutesByDistrictId(districtId, activeStatuses);
    }
}