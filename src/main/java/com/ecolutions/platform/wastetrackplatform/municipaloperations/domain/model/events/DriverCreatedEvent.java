package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class DriverCreatedEvent extends ApplicationEvent {
    private final String driverId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String documentNumber;
    private final String phoneNumber;
    private final String districtId;
    private final LocalDateTime occurredAt;

    private DriverCreatedEvent(Builder builder) {
        super(builder.source);
        this.driverId = builder.driverId;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.documentNumber = builder.documentNumber;
        this.phoneNumber = builder.phoneNumber;
        this.districtId = builder.districtId;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String driverId;
        private String email;
        private String firstName;
        private String lastName;
        private String documentNumber;
        private String phoneNumber;
        private String districtId;

        private Builder() {}

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder driverId(String driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder documentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder districtId(String districtId) {
            this.districtId = districtId;
            return this;
        }

        public DriverCreatedEvent build() {
            return new DriverCreatedEvent(this);
        }
    }
}
