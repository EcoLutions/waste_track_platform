package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import com.google.maps.model.LatLng;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * Value object representing geographic coordinates.
 * Uses precision=10, scale=8 to store coordinates with ~1mm accuracy.
 * Example: -12.04318380 (latitude), -77.02824040 (longitude)
 *
 * @author Salim Ramirez
 */
@Embeddable
public record Location(
    @Column(precision = 10, scale = 8)
    BigDecimal latitude,

    @Column(precision = 11, scale = 8)
    BigDecimal longitude
) {
    public Location {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Latitude and Longitude cannot be null");
        }
        if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }

    public LatLng toGoogleLatLng() {
        return new LatLng(latitude.doubleValue(), longitude.doubleValue());
    }

    public static Location fromGoogleLatLng(LatLng latLng) {
        return new Location(BigDecimal.valueOf(latLng.lat), BigDecimal.valueOf(latLng.lng));
    }

    public static Location fromStrings(String latStr, String lonStr) {
        try {
            BigDecimal latitude = new BigDecimal(latStr);
            BigDecimal longitude = new BigDecimal(lonStr);
            return new Location(latitude, longitude);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid latitude or longitude format", e);
        }
    }

    public static String latitudeAsStringOrNull(Location location) {
        return location != null ? location.latitude().toString() : null;
    }

    public static String longitudeAsStringOrNull(Location location) {
        return location != null ? location.longitude().toString() : null;
    }

    public static Location fromBigDecimal(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Latitude and Longitude cannot be null");
        }
        return new Location(latitude, longitude);
    }
    public double distanceTo(Location other) {
        double lat1 = this.latitude.doubleValue();
        double lon1 = this.longitude.doubleValue();
        double lat2 = other.latitude.doubleValue();
        double lon2 = other.longitude.doubleValue();

        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
