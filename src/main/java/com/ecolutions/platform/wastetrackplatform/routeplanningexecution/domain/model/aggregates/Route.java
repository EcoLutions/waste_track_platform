package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.VehicleId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Route extends AuditableAbstractAggregateRoot<Route> {
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id"))
    private DistrictId districtId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "driver_id"))
    private DriverId driverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteType routeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RouteStatus status;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "route_id")
    @OrderBy("sequenceOrder ASC")
    private Set<WayPoint> waypoints;

    @Embedded
    private Distance totalDistance;

    @Column(name = "estimated_duration")
    private Duration estimatedDuration;

    @Column(name = "actual_duration")
    private Duration actualDuration;

    public Route() {
        super();
        this.status = RouteStatus.DRAFT;
        this.routeType = RouteType.REGULAR;
        this.waypoints = new HashSet<>();
    }

    public Route(DistrictId districtId, RouteType routeType, LocalDate scheduledDate) {
        this();
        this.districtId = districtId;
        this.routeType = routeType;
        this.scheduledDate = scheduledDate;
    }

    public void addWaypoint(ContainerId containerId, Priority priority) {
        if (canBeModified()) {
            throw new IllegalStateException("Cannot modify route that is in progress or completed");
        }

        int sequenceOrder = waypoints.size() + 1;
        WayPoint waypoint = new WayPoint(containerId, sequenceOrder, priority);
        waypoints.add(waypoint);
    }

    public void removeWaypoint(String waypointId) {
        if (canBeModified()) {
            throw new IllegalStateException("Cannot modify route that is in progress or completed");
        }

        waypoints.removeIf(wp -> wp.getId().equals(waypointId));
        for (int i = 0; i < waypoints.size(); i++) {
            int finalI = i;
            int finalI1 = i;
            waypoints.stream()
                .filter(wp -> wp.getSequenceOrder() == finalI + 2)
                .findFirst()
                .ifPresent(wp -> wp.setSequenceOrder(finalI1 + 1));
        }
    }

    public void assignToDriver(DriverId driverId, VehicleId vehicleId) {
        if (status != RouteStatus.DRAFT) {
            throw new IllegalStateException("Can only assign driver to draft routes");
        }

        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = RouteStatus.ASSIGNED;
    }

    public void startExecution() {
        if (status != RouteStatus.ASSIGNED) {
            throw new IllegalStateException("Can only start assigned routes");
        }

        this.startedAt = LocalDateTime.now();
        this.status = RouteStatus.IN_PROGRESS;
    }

    public void completeExecution() {
        if (status != RouteStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete routes that are in progress");
        }

        this.completedAt = LocalDateTime.now();
        this.actualDuration = Duration.between(startedAt, completedAt);
        this.status = RouteStatus.COMPLETED;
    }

    public void markWaypointAsVisited(String waypointId, LocalDateTime timestamp) {
        WayPoint waypoint = waypoints.stream()
            .filter(wp -> wp.getId().equals(waypointId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Waypoint not found: " + waypointId));

        waypoint.markAsVisited(timestamp, Duration.ofMinutes(5)); // Default service time
    }

    public boolean canBeModified() {
        return status != RouteStatus.DRAFT && status != RouteStatus.ASSIGNED;
    }

    public boolean isOverdue() {
        if (status == RouteStatus.COMPLETED || status == RouteStatus.CANCELLED) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledDateTime = scheduledDate.atStartOfDay();

        return now.isAfter(scheduledDateTime.plusHours(2));
    }

    public void addWayPoint(WayPoint wayPoint) {
        this.waypoints.add(wayPoint);
    }
}
