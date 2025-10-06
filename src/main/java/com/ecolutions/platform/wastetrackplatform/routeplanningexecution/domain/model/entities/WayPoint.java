package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.entities.AuditableModel;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
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

    @Column(name = "service_time")
    private Duration serviceTime;

    @Column(name = "driver_note")
    private String driverNote;

    public WayPoint() {
        super();
        this.status = WaypointStatus.PENDING;
    }

    public WayPoint(ContainerId containerId, Integer sequenceOrder, Priority priority) {
        this();
        this.containerId = containerId;
        this.sequenceOrder = sequenceOrder;
        this.priority = priority;
    }

    public void markAsVisited(LocalDateTime arrivalTime, Duration serviceTime) {
        this.actualArrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.status = WaypointStatus.VISITED;
    }

    public void markAsSkipped(String reason) {
        this.driverNote = reason;
        this.status = WaypointStatus.SKIPPED;
    }

    public boolean isCompleted() {
        return status == WaypointStatus.VISITED;
    }

    public boolean canBeVisited() {
        return status == WaypointStatus.PENDING;
    }
}
