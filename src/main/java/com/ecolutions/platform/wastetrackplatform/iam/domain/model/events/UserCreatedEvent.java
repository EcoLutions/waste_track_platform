package com.ecolutions.platform.wastetrackplatform.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.List;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final String email;
    private final String temporalPassword;
    private final List<String> roles;

    private UserCreatedEvent(Builder builder) {
        super(builder.source);
        this.email = builder.email;
        this.temporalPassword = builder.temporalPassword;
        this.roles = builder.roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String email;
        private String temporalPassword;
        private List<String> roles;

        private Builder() {
        }

        public Builder source(Object source) {
            this.source = source;
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

        public UserCreatedEvent build() {
            return new UserCreatedEvent(this);
        }
    }
}