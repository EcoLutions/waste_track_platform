package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetAllWayPointsQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointByIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.queries.GetWayPointsByRouteIdQuery;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command.WayPointCommandService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.queries.WayPointQueryService;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.CreateWayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.MarkWayPointAsVisitedResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.request.UpdateWayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.WayPointResource;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromentitytoresponse.WayPointResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand.CreateWayPointCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand.MarkWayPointAsVisitedCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromresourcetocommand.UpdateWayPointCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.swagger.WayPointController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WayPointControllerImpl implements WayPointController {
    private final WayPointCommandService wayPointCommandService;
    private final WayPointQueryService wayPointQueryService;

    @Override
    public ResponseEntity<WayPointResource> createWayPoint(CreateWayPointResource resource, String routeId) {
        var command = CreateWayPointCommandFromResourceAssembler.toCommandFromResource(resource, routeId);
        var createdWayPoint = wayPointCommandService.handle(command);
        if (createdWayPoint.isEmpty()) return ResponseEntity.badRequest().build();
        var wayPointResource = WayPointResourceFromEntityAssembler.toResourceFromEntity(createdWayPoint.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(wayPointResource.id())
                .toUri();
        return ResponseEntity.created(location).body(wayPointResource);
    }

    @Override
    public ResponseEntity<WayPointResource> getWayPointById(String id) {
        var query = new GetWayPointByIdQuery(id);
        var wayPoint = wayPointQueryService.handle(query);
        if (wayPoint.isEmpty()) return ResponseEntity.notFound().build();
        var wayPointResource = WayPointResourceFromEntityAssembler.toResourceFromEntity(wayPoint.get());
        return ResponseEntity.status(HttpStatus.OK).body(wayPointResource);
    }

    @Override
    public ResponseEntity<List<WayPointResource>> getAllWayPoints(String routeId) {
        var wayPoints = List.<WayPoint>of();
        if (routeId != null && !routeId.isBlank()) {
            var query = new GetWayPointsByRouteIdQuery(routeId);
            wayPoints = wayPointQueryService.handle(query);
        } else if (routeId == null || routeId.isBlank()) {
            var query = new GetAllWayPointsQuery();
            wayPoints = wayPointQueryService.handle(query);
        }
        var wayPointResources = wayPoints.stream()
                .map(WayPointResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(wayPointResources);
    }

    @Override
    public ResponseEntity<WayPointResource> updateWayPoint(String id, UpdateWayPointResource resource) {
        var command = UpdateWayPointCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedWayPoint = wayPointCommandService.handle(command);
        if (updatedWayPoint.isEmpty()) return ResponseEntity.notFound().build();
        var wayPointResource = WayPointResourceFromEntityAssembler.toResourceFromEntity(updatedWayPoint.get());
        return ResponseEntity.status(HttpStatus.OK).body(wayPointResource);
    }

    @Override
    public ResponseEntity<Void> deleteWayPoint(String id) {
        var command = new com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteWayPointCommand(id);
        var result = wayPointCommandService.handle(command);
        if (!result) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<WayPointResource> markWayPointAsVisited(String id, MarkWayPointAsVisitedResource resource) {
        var command = MarkWayPointAsVisitedCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedWayPoint = wayPointCommandService.handle(command);
        if (updatedWayPoint.isEmpty()) return ResponseEntity.notFound().build();
        var wayPointResource = WayPointResourceFromEntityAssembler.toResourceFromEntity(updatedWayPoint.get());
        return ResponseEntity.status(HttpStatus.OK).body(wayPointResource);
    }
}