package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.CreateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.commands.UpdateRouteCommand;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.entities.WayPoint;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.VehicleId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

    private LocalDateTime scheduledStartAt;

    private LocalDateTime scheduledEndAt;

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

    @Column(name = "collection_duration")
    private Duration collectionDuration;

    @Column(name = "return_duration")
    private Duration returnDuration;

    @Column(name = "actual_duration")
    private Duration actualDuration;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "current_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "current_longitude"))
    })
    private Location currentLocation;

    private LocalDateTime lastLocationUpdate;

    public Route() {
        super();
        this.status = RouteStatus.ASSIGNED;
        this.routeType = RouteType.REGULAR;
        this.waypoints = new HashSet<>();
    }

    public Route(CreateRouteCommand command) {
        this();
        this.districtId = DistrictId.of(command.districtId());
        this.driverId = DriverId.of(command.driverId());
        this.vehicleId = VehicleId.of(command.vehicleId());
        this.routeType = RouteType.fromString(command.routeType());
        this.scheduledStartAt = command.scheduledDate();
    }

    public void update(UpdateRouteCommand command) {
        if (command.scheduledStartAt() != null) {
            this.scheduledStartAt = command.scheduledStartAt();
        }
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

    private void reorderWaypoints() {
        List<WayPoint> sortedWaypoints = waypoints.stream()
                .sorted(Comparator.comparing(WayPoint::getSequenceOrder))
                .toList();

        for (int i = 0; i < sortedWaypoints.size(); i++) {
            sortedWaypoints.get(i).setSequenceOrder(i + 1);
        }
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

        waypoint.markAsVisited(timestamp);
    }

    public boolean canBeModified() {
        return status == RouteStatus.ASSIGNED;
    }

    public boolean isOverdue() {
        if (status == RouteStatus.COMPLETED || status == RouteStatus.CANCELLED) return false;
        return LocalDateTime.now().isAfter(scheduledStartAt);
    }

    public void addWayPoint(WayPoint wayPoint) {
        if (!canBeModified()) {
            throw new IllegalStateException("Cannot modify route that is in progress or completed");
        }
        this.waypoints.add(wayPoint);
    }

    public void updateCurrentLocation(Location location) {
        if (status != RouteStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only update location for routes in progress");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.currentLocation = location;
        this.lastLocationUpdate = LocalDateTime.now();
    }

    public void calculateEstimatedArrivalTimes(LocalTime startTime) {
        if (estimatedDuration == null || waypoints.isEmpty()) {
            return;
        }

        long totalMinutes = estimatedDuration.toMinutes();
        long minutesPerWaypoint = totalMinutes / waypoints.size();

        List<WayPoint> sortedWaypoints = waypoints.stream()
                .sorted(Comparator.comparing(WayPoint::getSequenceOrder))
                .toList();

        for (WayPoint waypoint : sortedWaypoints) {
            waypoint.setEstimatedArrivalTime(scheduledStartAt);
            scheduledStartAt = scheduledStartAt.plusMinutes(minutesPerWaypoint);
        }
    }

    public void updateEstimates(Distance distance, Duration duration) {
        if (!canBeModified()) {
            throw new IllegalStateException("Can only update estimates for assigned routes");
        }
        this.totalDistance = distance;
        this.estimatedDuration = duration;
    }

    public int getCompletedWaypointsCount() {
        return (int) waypoints.stream()
                .filter(WayPoint::isCompleted)
                .count();
    }

    public int getTotalWaypointsCount() {
        return waypoints.size();
    }

    public double getProgressPercentage() {
        if (waypoints.isEmpty()) return 0.0;
        return (double) getCompletedWaypointsCount() / getTotalWaypointsCount() * 100.0;
    }

    public void cancel(String reason) {
        if (status == RouteStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed routes");
        }
        this.status = RouteStatus.CANCELLED;
    }

    public LocalDateTime getEstimatedDisposalArrival() {
        if (collectionDuration == null) return null;
        return scheduledStartAt.plus(collectionDuration);
    }

    public LocalDateTime getEstimatedDepotReturn() {
        if (estimatedDuration == null) return null;
        return scheduledStartAt.plus(estimatedDuration);
    }

    public boolean isWithinTimeConstraint(Duration maxDuration) {
        if (estimatedDuration == null) return true;
        return estimatedDuration.compareTo(maxDuration) <= 0;
    }

    public void updateDurations(Duration collection, Duration returnTrip) {
        this.collectionDuration = collection;
        this.returnDuration = returnTrip;
        this.estimatedDuration = collection.plus(returnTrip);
    }
}
