package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class DistrictCreatedEvent extends ApplicationEvent {

    private final String districtId;
    private final String name;
    private final String code;
    private final String primaryAdminEmail;
    private final String planId;
    private final LocalDateTime occurredAt;

    private DistrictCreatedEvent(Builder builder) {
        super(builder.source);
        this.districtId = builder.districtId;
        this.name = builder.name;
        this.code = builder.code;
        this.primaryAdminEmail = builder.primaryAdminEmail;
        this.planId = builder.planId;
        this.occurredAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Object source;
        private String districtId;
        private String name;
        private String code;
        private String primaryAdminEmail;
        private String planId;

        private Builder() {}

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder districtId(String districtId) {
            this.districtId = districtId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder primaryAdminEmail(String primaryAdminEmail) {
            this.primaryAdminEmail = primaryAdminEmail;
            return this;
        }

        public Builder planId(String planId) {
            this.planId = planId;
            return this;
        }

        public DistrictCreatedEvent build() {
            return new DistrictCreatedEvent(this);
        }
    }
}