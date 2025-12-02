package com.ecolutions.platform.wastetrackplatform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class PasswordResetRequestedEvent extends ApplicationEvent {
    private final String userId;
    private final String email;
    private final String username;
    private final String resetToken;
    private final LocalDateTime occurredAt;

    private PasswordResetRequestedEvent(Builder builder) {
        super(builder.source);
        this.userId = builder.userId;
        this.email = builder.email;
        this.username = builder.username;
        this.resetToken = builder.resetToken;
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
        private String resetToken;

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

        public Builder resetToken(String resetToken) {
            this.resetToken = resetToken;
            return this;
        }

        public PasswordResetRequestedEvent build() {
            return new PasswordResetRequestedEvent(this);
        }
    }
}