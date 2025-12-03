package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WayPointAsVisitedEvent extends ApplicationEvent {
    private final String wayPointId;
    private final String containerId;


    private WayPointAsVisitedEvent(Builder builder) {
        super(builder.source);
        this.wayPointId = builder.wayPointId;
        this.containerId = builder.containerId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String wayPointId;
        private String containerId;

        private Builder() {}

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder wayPointId(String wayPointId) {
            this.wayPointId = wayPointId;
            return this;
        }

        public Builder containerId(String containerId) {
            this.containerId = containerId;
            return this;
        }

        public WayPointAsVisitedEvent build() {
            return new WayPointAsVisitedEvent(this);
        }
    }
}
