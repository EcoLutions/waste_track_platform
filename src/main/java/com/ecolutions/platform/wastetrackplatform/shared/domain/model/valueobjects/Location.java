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
}
