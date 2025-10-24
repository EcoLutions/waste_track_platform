package com.ecolutions.platform.wastetrackplatform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final String userId;
    private final String email;
    private final String temporalPassword;
    private final List<String> roles;
    private final String districtId;
    private final LocalDateTime occurredAt;

    private UserCreatedEvent(Builder builder) {
        super(builder.source);
        this.userId = builder.userId;
        this.email = builder.email;
        this.temporalPassword = builder.temporalPassword;
        this.roles = builder.roles;
        this.districtId = builder.districtId;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String userId;
        private String email;
        private String temporalPassword;
        private List<String> roles;
        private String districtId;

        private Builder() {
        }

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder temporalPassword(String temporalPassword) {
            this.temporalPassword = temporalPassword;
            return this;
        }

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder districtId(String districtId) {
            this.districtId = districtId;
            return this;
        }

        public UserCreatedEvent build() {
            return new UserCreatedEvent(this);
        }
    }
}