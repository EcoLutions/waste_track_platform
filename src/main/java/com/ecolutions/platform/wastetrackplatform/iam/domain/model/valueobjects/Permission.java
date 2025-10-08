package com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Permission(String resource, String action) {
    public Permission {
        if (resource == null || resource.isBlank()) {
            throw new IllegalArgumentException("Permission resource cannot be null or blank");
        }
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Permission action cannot be null or blank");
        }
        if (resource.length() > 100) {
            throw new IllegalArgumentException("Permission resource cannot exceed 100 characters");
        }
        if (action.length() > 50) {
            throw new IllegalArgumentException("Permission action cannot exceed 50 characters");
        }
    }

    public static Permission of(String resource, String action) {
        if (resource == null || resource.isBlank() || action == null || action.isBlank()) {
            return null;
        }
        return new Permission(resource, action);
    }

    public static String toStringOrNull(Permission permission) {
        return permission != null ? permission.resource() + ":" + permission.action() : null;
    }

    public static Permission fromString(String permissionString) {
        if (permissionString == null || permissionString.isBlank()) {
            throw new IllegalArgumentException("Permission string cannot be null or blank");
        }
        String[] parts = permissionString.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Permission string must be in format 'resource:action'");
        }
        return new Permission(parts[0], parts[1]);
    }

    public static String resourceOrNull(Permission permission) {
        return permission != null ? permission.resource() : null;
    }

    public static String actionOrNull(Permission permission) {
        return permission != null ? permission.action() : null;
    }
}