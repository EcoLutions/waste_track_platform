package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateCurrentLocationRouteCommand;
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
        var command = new com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteRouteCommand(id);
        var result = routeCommandService.handle(command);
        if (!result) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RouteResource> getRouteById(String id) {
        var query = new com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetRouteByIdQuery(id);
        var route = routeQueryService.handle(query);
        if (route.isEmpty()) return ResponseEntity.notFound().build();
        var routeResource = RouteResourceFromEntityAssembler.toResourceFromEntity(route.get());
        return ResponseEntity.status(HttpStatus.OK).body(routeResource);
    }

    @Override
    public ResponseEntity<List<RouteResource>> getAllRoutes() {
        var query = new com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllRoutesQuery();
        var routes = routeQueryService.handle(query);
        var routeResources = routes.stream()
                .map(RouteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(routeResources);
    }

    @Override
    public ResponseEntity<List<RouteResource>> getActiveRoutesByDistrictId(String districtId) {
        var query = new com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetActiveRoutesByDistrictIdQuery(districtId);
        var routes = routeQueryService.handle(query);
        var routeResources = routes.stream()
                .map(RouteResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(routeResources);
    }

    @Override
    public ResponseEntity<RouteResource> generateOptimizedWaypoints(String id) {
        var command = new com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.GenerateOptimizedWaypointsCommand(id);
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
}