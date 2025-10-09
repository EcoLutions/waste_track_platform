package com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects;

public enum Language {
    ES,
    EN,
    PT,
    FR;

    public static Language fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Language cannot be null or blank");
        }
        try {
            return Language.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Language: " + value);
        }
    }

    public static String toStringOrNull(Language language) {
        return language != null ? language.name() : null;
    }
}