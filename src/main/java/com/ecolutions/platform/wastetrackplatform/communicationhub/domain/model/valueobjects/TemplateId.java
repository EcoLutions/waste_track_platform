package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TemplateId(String value) {
    public TemplateId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Template ID cannot be null or blank");
        }
    }

    public static TemplateId of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new TemplateId(value);
    }

    public static String toStringOrNull(TemplateId templateId) {
        return templateId != null ? templateId.value() : null;
    }
}