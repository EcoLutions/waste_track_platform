package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.DeleteWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.MarkWayPointAsVisitedCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;

import java.util.Optional;

public interface WayPointCommandService {
    Optional<WayPoint> handle(CreateWayPointCommand command);
    Optional<WayPoint> handle(UpdateWayPointCommand command);
    Boolean handle(DeleteWayPointCommand command);
    Optional<WayPoint> handle(MarkWayPointAsVisitedCommand command);
}