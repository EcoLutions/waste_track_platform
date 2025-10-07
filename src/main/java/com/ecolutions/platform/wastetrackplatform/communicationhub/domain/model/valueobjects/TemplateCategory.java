package com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.valueobjects;

public enum TemplateCategory {
    OPERATIONAL,
    ADMINISTRATIVE,
    MARKETING;

    public static TemplateCategory fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TemplateCategory cannot be null or blank");
        }
        try {
            return TemplateCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid TemplateCategory: " + value);
        }
    }
}