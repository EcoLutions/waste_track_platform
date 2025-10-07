package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Location(
    BigDecimal latitude,
    BigDecimal longitude,
    String address,
    String districtCode
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

    public static Location fromStrings(String latStr, String lonStr, String address, String districtCode) {
        try {
            BigDecimal latitude = new BigDecimal(latStr);
            BigDecimal longitude = new BigDecimal(lonStr);
            return new Location(latitude, longitude, address, districtCode);
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

    public static String addressOrNull(Location location) {
        return location != null ? location.address() : null;
    }

    public static String districtCodeOrNull(Location location) {
        return location != null ? location.districtCode() : null;
    }
}
