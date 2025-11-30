package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class DeviceCreatedEvent extends ApplicationEvent {

    private final String deviceId;
    private final String deviceIdentifier;
    private final LocalDateTime occurredAt;

    private DeviceCreatedEvent(Builder builder) {
        super(builder.source);
        this.deviceId = builder.deviceId;
        this.deviceIdentifier = builder.deviceIdentifier;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Object source;
        private String deviceId;
        private String deviceIdentifier;

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder deviceIdentifier(String deviceIdentifier) {
            this.deviceIdentifier = deviceIdentifier;
            return this;
        }

        public DeviceCreatedEvent build() {
            return new DeviceCreatedEvent(this);
        }
    }
}