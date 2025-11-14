package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events;

import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ContainerBecameCriticalEvent extends ApplicationEvent {
    private final String containerId;
    private final String districtId;
    private final Location location;
    private final Integer fillLevel;
    private final LocalDateTime occurredAt;

    private ContainerBecameCriticalEvent(Builder builder) {
        super(builder.source);
        this.containerId = builder.containerId;
        this.districtId = builder.districtId;
        this.location = builder.location;
        this.fillLevel = builder.fillLevel;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String containerId;
        private String districtId;
        private Location location;
        private Integer fillLevel;

        private Builder() {}

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder containerId(String containerId) {
            this.containerId = containerId;
            return this;
        }

        public Builder districtId(String districtId) {
            this.districtId = districtId;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder fillLevel(Integer fillLevel) {
            this.fillLevel = fillLevel;
            return this;
        }

        public ContainerBecameCriticalEvent build() {
            return new ContainerBecameCriticalEvent(this);
        }
    }
}
