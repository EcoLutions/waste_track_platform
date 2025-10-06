package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record GeographicBoundaries(String boundaryPolygon) {
    public GeographicBoundaries {
        if (boundaryPolygon == null || boundaryPolygon.isBlank()) {
            throw new IllegalArgumentException("Boundary polygon cannot be null or blank");
        }
        if (!boundaryPolygon.trim().startsWith("{") ||
                !boundaryPolygon.contains("\"type\"") ||
                !boundaryPolygon.contains("\"Polygon\"") ||
                !boundaryPolygon.contains("\"coordinates\"")) {
            throw new IllegalArgumentException("Boundary polygon must be a valid GeoJSON Polygon");
        }
    }
}