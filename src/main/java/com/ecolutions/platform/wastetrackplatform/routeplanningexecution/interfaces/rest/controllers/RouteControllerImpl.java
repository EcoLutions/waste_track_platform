package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.*;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetActiveRoutesByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllRoutesQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetRouteByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.RouteCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.RouteQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateRouteResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateCurrentLocationResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateRouteResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.RouteResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand.CreateRouteCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand.UpdateRouteCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromentitytoresponse.RouteResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.swagger.RouteController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteControllerImpl implements RouteController {
    private final RouteCommandService routeCommandService;
    private final RouteQueryService routeQueryService;

    @Override
    public ResponseEntity<RouteResource> createRoute(CreateRouteResource resource) {
        var command = CreateRouteCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdRoute = routeCommandService.handle(command);
        if (createdRoute.isEmpty()) return ResponseEntity.badRequest().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(createdRoute.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(routeResource.id())
                .toUri();
        return ResponseEntity.created(location).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> updateRoute(String id, UpdateRouteResource resource) {
        var command = UpdateRouteCommandFromResourceAssembler.toCommandFromResource(resource);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<Void> deleteRoute(String id) {
        var command = new DeleteRouteCommand(id);
        var result = routeCommandService.handle(command);
        if (!result) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RouteResource> getRouteById(String id) {
        var query = new GetRouteByIdQuery(id);
        var route = routeQueryService.handle(query);
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<List<RouteResource>> getAllRoutes(
            String districtId,
            String driverId,
            String vehicleId,
            String status,
            List<String> statuses
    ) {
        var query = GetAllRoutesQuery.builder()
                .districtId(districtId)
                .driverId(driverId)
                .vehicleId(vehicleId)
                .status(status)
                .statuses(statuses)
                .build();

        var routes = routeQueryService.handle(query);
        var routeResources = routes.stream()
                .map(RouteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(routeResources);
    }

    @Override
    public ResponseEntity<List<RouteResource>> getActiveRoutesByDistrictId(String districtId) {
        var query = new GetActiveRoutesByDistrictIdQuery(districtId);
        var routes = routeQueryService.handle(query);
        var routeResources = routes.stream()
                .map(RouteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(routeResources);
    }

    @Override
    public ResponseEntity<RouteResource> generateOptimizedWaypoints(String id) {
        var command = new GenerateOptimizedWaypointsCommand(id);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> updateCurrentLocation(String id, UpdateCurrentLocationResource resource) {
        var command = new UpdateCurrentLocationRouteCommand(id, resource.latitude(), resource.longitude());
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> startRoute(String id) {
        var command = new StartRouteCommand(id);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> completeRoute(String id) {
        var command = new CompleteRouteCommand(id);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> cancelRoute(String id) {
        var command = new CancelRouteCommand(id);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> reOptimizeRoute(String id) {
        var command = new ReOptimizeRouteCommand(id);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> updateRouteEstimates(String id) {
        var command = new UpdateRouteEstimatesCommand(id);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<RouteResource> markWaypointAsVisited(String id, String waypointId) {
        var command = new MarkWayPointAsVisitedCommand(id, waypointId);
        var updatedRoute = routeCommandService.handle(command);
        if (updatedRoute.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(updatedRoute.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }
}