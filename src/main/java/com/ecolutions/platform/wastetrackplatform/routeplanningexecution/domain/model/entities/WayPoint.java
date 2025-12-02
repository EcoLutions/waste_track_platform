package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateWayPointCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.Priority;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.PriorityLevel;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.WaypointStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.entities.AuditableModel;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WayPoint extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "container_id"))
    private ContainerId containerId;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Embedded
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaypointStatus status;

    @Column(name = "estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;

    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    public WayPoint() {
        super();
        this.status = WaypointStatus.PENDING;
    }

    public WayPoint(CreateWayPointCommand command) {
        this();
        this.containerId = ContainerId.of(command.containerId());
        this.sequenceOrder = command.sequenceOrder();
        this.priority = new Priority(PriorityLevel.fromString(command.priority()));
    }

    public void update(UpdateWayPointCommand command) {
        if (command.sequenceOrder() != null) {
            this.sequenceOrder = command.sequenceOrder();
        }
        if (command.priority() != null) {
            this.priority = new Priority(PriorityLevel.fromString(command.priority()));
        }
        if (command.estimatedArrivalTime() != null) {
            this.estimatedArrivalTime = command.estimatedArrivalTime();
        }
    }

    public void markAsVisited() {
        if (!canBeVisited()) {
            throw new IllegalStateException("Waypoint cannot be visited in current state: " + status);
        }
        this.actualArrivalTime = LocalDateTime.now();
        this.status = WaypointStatus.VISITED;
    }

    public boolean isCompleted() {
        return status == WaypointStatus.VISITED;
    }

    public boolean canBeVisited() {
        return status == WaypointStatus.PENDING;
    }

    public void updatePriority(Priority newPriority) {
        if (status != WaypointStatus.PENDING) {
            throw new IllegalStateException("Cannot update priority of non-pending waypoint");
        }
        this.priority = newPriority;
    }
}