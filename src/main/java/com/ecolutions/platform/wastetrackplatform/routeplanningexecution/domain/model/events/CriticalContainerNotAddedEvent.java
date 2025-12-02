package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Domain event published when a critical container cannot be added to a route
 */
@Getter
public class CriticalContainerNotAddedEvent extends ApplicationEvent {
    private final String routeId;
    private final String districtId;
    private final String containerId;
    private final Location containerLocation;
    private final Integer fillLevel;
    private final String reason;
    private final LocalDateTime occurredAt;

    private CriticalContainerNotAddedEvent(Builder builder) {
        super(builder.source);
        this.routeId = builder.routeId;
        this.districtId = builder.districtId;
        this.containerId = builder.containerId;
        this.containerLocation = builder.containerLocation;
        this.fillLevel = builder.fillLevel;
        this.reason = builder.reason;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String routeId;
        private String districtId;
        private String containerId;
        private Location containerLocation;
        private Integer fillLevel;
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

        public Builder districtId(String districtId) {
            this.districtId = districtId;
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

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public CriticalContainerNotAddedEvent build() {
            return new CriticalContainerNotAddedEvent(this);
        }
    }
}
