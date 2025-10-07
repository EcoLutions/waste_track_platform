package com.ecolutions.platform.wastetrackplatform.profile.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Photo(String filePath) {
    public Photo {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Photo filePath cannot be null or blank");
        }
        if (!filePath.matches("^[a-zA-Z0-9/_.-]+$")) {
            throw new IllegalArgumentException("Photo filePath contains invalid characters");
        }
    }

    public static Photo of(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return null;
        }
        return new Photo(filePath);
    }

    public static String toStringOrNull(Photo photo) {
        return photo != null ? photo.filePath() : null;
    }
}