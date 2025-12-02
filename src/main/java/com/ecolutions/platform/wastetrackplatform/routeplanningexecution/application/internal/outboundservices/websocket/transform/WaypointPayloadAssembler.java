package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.application.internal.outboundservices.websocket.transform;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.CriticalContainerRejectedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.WaypointAddedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.WaypointReplacedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.payloads.WaypointRemovedPayload;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Priority;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;

import java.time.LocalDateTime;

public class WaypointPayloadAssembler {

    public static WaypointAddedPayload toAddedPayload(String routeId, WayPoint waypoint, ContainerInfoDTO container, String reason) {
        return WaypointAddedPayload.builder()
                .routeId(routeId)
                .waypointId(waypoint.getId())
                .containerId(container.containerId())
                .sequenceOrder(waypoint.getSequenceOrder())
                .priority(Priority.toStringOrNull(waypoint.getPriority()))
                .latitude(Location.latitudeAsStringOrNull(container.location()))
                .longitude(Location.longitudeAsStringOrNull(container.location()))
                .action("ADDED")
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static WaypointReplacedPayload toReplacedPayload(String routeId, WayPoint removedWaypoint, ContainerInfoDTO removedContainer, WayPoint addedWaypoint, ContainerInfoDTO addedContainer, String reason) {
        return WaypointReplacedPayload.builder()
                .routeId(routeId)
                .removedWaypointId(removedWaypoint.getId())
                .removedContainerId(removedContainer.containerId())
                .addedWaypointId(addedWaypoint.getId())
                .addedContainerId(addedContainer.containerId())
                .sequenceOrder(addedWaypoint.getSequenceOrder())
                .priority(Priority.toStringOrNull(addedWaypoint.getPriority()))
                .latitude(Location.latitudeAsStringOrNull(addedContainer.location()))
                .longitude(Location.longitudeAsStringOrNull(addedContainer.location()))
                .action("REPLACED")
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static WaypointRemovedPayload toRemovedPayload(String routeId, WayPoint waypoint, String containerId, String reason) {
        return WaypointRemovedPayload.builder()
                .routeId(routeId)
                .waypointId(waypoint.getId())
                .containerId(containerId)
                .action("REMOVED")
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static CriticalContainerRejectedPayload toCriticalRejectedPayload(String routeId, String containerId, Location location, Integer fillLevel, String reason
    ) {
        return CriticalContainerRejectedPayload.builder()
                .routeId(routeId)
                .containerId(containerId)
                .latitude(Location.latitudeAsStringOrNull(location))
                .longitude(Location.longitudeAsStringOrNull(location))
                .fillLevel(fillLevel)
                .reason(reason)
                .action("REJECTED")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
