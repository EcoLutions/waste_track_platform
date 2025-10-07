package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record LicensePlate(String value) {
    // Formato común de placa en Perú: 3 letras + guion opcional + 3 dígitos (e.g. ABC-123 o ABC123)
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^[A-Z]{3}-?\\d{3}$");

    public LicensePlate {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La placa no puede ser nula ni vacía");
        }

        value = value.trim().toUpperCase();

        if (!LICENSE_PLATE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de placa inválido. Se espera 3 letras (mayúsculas) y 3 dígitos, por ejemplo: ABC-123");
        }
    }

    public static String toStringOrNull(LicensePlate licensePlate) {
        return licensePlate == null ? null : licensePlate.value();
    }
}