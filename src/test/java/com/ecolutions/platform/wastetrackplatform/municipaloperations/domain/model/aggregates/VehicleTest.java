package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.LicensePlate;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.Mileage;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.VolumeCapacity;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.WeightCapacity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    @DisplayName("Should create a valid license plate when format is correct")
    void shouldCreateValidLicensePlateWhenFormatIsCorrect() {
        // Arrange
        String plateValue = "ABC-123";

        // Act
        LicensePlate licensePlate = new LicensePlate(plateValue);

        // Assert
        assertNotNull(licensePlate);
        assertEquals("ABC-123", licensePlate.value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when license plate format is invalid")
    void shouldThrowExceptionWhenLicensePlateFormatIsInvalid() {
        // Arrange
        String invalidPlate = "12-ABCD";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new LicensePlate(invalidPlate)
        );

        assertTrue(exception.getMessage().contains("Formato de placa inválido"));
    }

    @Test
    @DisplayName("Should mark mileage as needing maintenance when exceeds 10,000 km")
    void shouldMarkMileageAsNeedingMaintenanceWhenExceedsLimit() {
        // Arrange
        Mileage mileage = new Mileage(15000);

        // Act
        boolean needsMaintenance = mileage.needsMaintenance();

        // Assert
        assertTrue(needsMaintenance);
    }

    @Test
    @DisplayName("Should throw exception when volume or weight capacity is invalid")
    void shouldThrowExceptionWhenCapacityValuesAreInvalid() {
        // Arrange, Act & Assert (VolumeCapacity)
        assertThrows(IllegalArgumentException.class, () ->
                new VolumeCapacity(BigDecimal.ZERO)
        );

        // Arrange, Act & Assert (WeightCapacity)
        assertThrows(IllegalArgumentException.class, () ->
                new WeightCapacity(-500)
        );
    }
}



