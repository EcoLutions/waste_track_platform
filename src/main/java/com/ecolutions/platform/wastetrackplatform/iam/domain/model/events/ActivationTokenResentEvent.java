package com.ecolutions.platform.wastetrackplatform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ActivationTokenResentEvent extends ApplicationEvent {
    private final String userId;
    private final String email;
    private final String username;
    private final String activationToken;
    private final LocalDateTime occurredAt;

    private ActivationTokenResentEvent(Builder builder) {
        super(builder.source);
        this.userId = builder.userId;
        this.email = builder.email;
        this.username = builder.username;
        this.activationToken = builder.activationToken;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String userId;
        private String email;
        private String username;
        private String activationToken;

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

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder activationToken(String activationToken) {
            this.activationToken = activationToken;
            return this;
        }

        public ActivationTokenResentEvent build() {
            return new ActivationTokenResentEvent(this);
        }
    }
}
