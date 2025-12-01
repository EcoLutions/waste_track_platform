package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ContainerFillLevelUpdatedEvent extends ApplicationEvent {
    private final String containerId;
    private final Float maxFillLevelThreshold;
    private final String sensorId;
    private final LocalDateTime occurredAt;

    private ContainerFillLevelUpdatedEvent(Builder builder) {
        super(builder.source);
        this.containerId = builder.containerId;
        this.maxFillLevelThreshold = builder.maxFillLevelThreshold;
        this.sensorId = builder.sensorId;
        this.occurredAt = builder.occurredAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String containerId;
        private Float maxFillLevelThreshold;
        private String sensorId;
        private LocalDateTime occurredAt;

        private Builder() {}

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder containerId(String containerId) {
            this.containerId = containerId;
            return this;
        }

        public Builder maxFillLevelThreshold(Float maxFillLevelThreshold) {
            this.maxFillLevelThreshold = maxFillLevelThreshold;
            return this;
        }

        public  Builder sensorId(String sensorId) {
            this.sensorId = sensorId;
            return this;
        }

        public Builder occurredAt(LocalDateTime occurredAt) {
            this.occurredAt = occurredAt;
            return this;
        }

        public ContainerFillLevelUpdatedEvent build() {
            return new ContainerFillLevelUpdatedEvent(this);
        }
    }

}
