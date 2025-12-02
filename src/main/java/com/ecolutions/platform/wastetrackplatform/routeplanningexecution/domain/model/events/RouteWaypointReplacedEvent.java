package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a waypoint is replaced in a route
 */
@Getter
public class RouteWaypointReplacedEvent extends ApplicationEvent {
    private final String routeId;
    private final String driverId;
    private final String districtId;
    private final String removedWaypointId;
    private final String removedContainerId;
    private final Location removedContainerLocation;
    private final String removedPriority;
    private final String addedWaypointId;
    private final String addedContainerId;
    private final Location addedContainerLocation;
    private final Integer addedFillLevel;
    private final String addedPriority;
    private final String reason;
    private final LocalDateTime occurredAt;

    private RouteWaypointReplacedEvent(Builder builder) {
        super(builder.source);
        this.routeId = builder.routeId;
        this.driverId = builder.driverId;
        this.districtId = builder.districtId;
        this.removedWaypointId = builder.removedWaypointId;
        this.removedContainerId = builder.removedContainerId;
        this.removedContainerLocation = builder.removedContainerLocation;
        this.removedPriority = builder.removedPriority;
        this.addedWaypointId = builder.addedWaypointId;
        this.addedContainerId = builder.addedContainerId;
        this.addedContainerLocation = builder.addedContainerLocation;
        this.addedFillLevel = builder.addedFillLevel;
        this.addedPriority = builder.addedPriority;
        this.reason = builder.reason;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String routeId;
        private String driverId;
        private String districtId;
        private String removedWaypointId;
        private String removedContainerId;
        private Location removedContainerLocation;
        private String removedPriority;
        private String addedWaypointId;
        private String addedContainerId;
        private Location addedContainerLocation;
        private Integer addedFillLevel;
        private String addedPriority;
        private String reason;

        private Builder() {}

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder routeId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public Builder driverId(String driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder districtId(String districtId) {
            this.districtId = districtId;
            return this;
        }

        public Builder removedWaypointId(String removedWaypointId) {
            this.removedWaypointId = removedWaypointId;
            return this;
        }

        public Builder removedContainerId(String removedContainerId) {
            this.removedContainerId = removedContainerId;
            return this;
        }

        public Builder removedContainerLocation(Location removedContainerLocation) {
            this.removedContainerLocation = removedContainerLocation;
            return this;
        }

        public Builder removedPriority(String removedPriority) {
            this.removedPriority = removedPriority;
            return this;
        }

        public Builder addedWaypointId(String addedWaypointId) {
            this.addedWaypointId = addedWaypointId;
            return this;
        }

        public Builder addedContainerId(String addedContainerId) {
            this.addedContainerId = addedContainerId;
            return this;
        }

        public Builder addedContainerLocation(Location addedContainerLocation) {
            this.addedContainerLocation = addedContainerLocation;
            return this;
        }

        public Builder addedFillLevel(Integer addedFillLevel) {
            this.addedFillLevel = addedFillLevel;
            return this;
        }

        public Builder addedPriority(String addedPriority) {
            this.addedPriority = addedPriority;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RouteWaypointReplacedEvent build() {
            return new RouteWaypointReplacedEvent(this);
        }
    }
}
