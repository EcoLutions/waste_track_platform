package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a waypoint is added to a route
 */
@Getter
public class RouteWaypointAddedEvent extends ApplicationEvent {
    private final String routeId;
    private final String driverId;
    private final String districtId;
    private final String waypointId;
    private final String containerId;
    private final Location containerLocation;
    private final Integer fillLevel;
    private final String priority;
    private final Integer sequenceOrder;
    private final String reason;
    private final LocalDateTime occurredAt;

    private RouteWaypointAddedEvent(Builder builder) {
        super(builder.source);
        this.routeId = builder.routeId;
        this.driverId = builder.driverId;
        this.districtId = builder.districtId;
        this.waypointId = builder.waypointId;
        this.containerId = builder.containerId;
        this.containerLocation = builder.containerLocation;
        this.fillLevel = builder.fillLevel;
        this.priority = builder.priority;
        this.sequenceOrder = builder.sequenceOrder;
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
        private String waypointId;
        private String containerId;
        private Location containerLocation;
        private Integer fillLevel;
        private String priority;
        private Integer sequenceOrder;
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

        public Builder waypointId(String waypointId) {
            this.waypointId = waypointId;
            return this;
        }

        public Builder containerId(String containerId) {
            this.containerId = containerId;
            return this;
        }

        public Builder containerLocation(Location containerLocation) {
            this.containerLocation = containerLocation;
            return this;
        }

        public Builder fillLevel(Integer fillLevel) {
            this.fillLevel = fillLevel;
            return this;
        }

        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public Builder sequenceOrder(Integer sequenceOrder) {
            this.sequenceOrder = sequenceOrder;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RouteWaypointAddedEvent build() {
            return new RouteWaypointAddedEvent(this);
        }
    }
}
