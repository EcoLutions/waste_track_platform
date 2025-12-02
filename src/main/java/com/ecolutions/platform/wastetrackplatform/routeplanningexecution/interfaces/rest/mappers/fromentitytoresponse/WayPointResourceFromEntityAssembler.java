package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Priority;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.WaypointStatus;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.interfaces.rest.dto.response.WayPointResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class WayPointResourceFromEntityAssembler {
    public static WayPointResource toResourceFromEntity(WayPoint wayPoint) {
        return WayPointResource.builder()
                .id(wayPoint.getId())
                .containerId(ContainerId.toStringOrNull(wayPoint.getContainerId()))
                .sequenceOrder(wayPoint.getSequenceOrder())
                .priority(Priority.toStringOrNull(wayPoint.getPriority()))
                .status(WaypointStatus.toStringOrNull(wayPoint.getStatus()))
                .estimatedArrivalTime(DateTimeUtils.localDateTimeToStringOrNull(wayPoint.getEstimatedArrivalTime()))
                .actualArrivalTime(DateTimeUtils.localDateTimeToStringOrNull(wayPoint.getActualArrivalTime()))
                .createdAt(DateTimeUtils.dateToStringOrNull(wayPoint.getCreatedAt()))
                .updatedAt(DateTimeUtils.dateToStringOrNull(wayPoint.getUpdatedAt()))
                .build();
    }
}