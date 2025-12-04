package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ValidationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensorReadingTest {

    private CreateSensorReadingCommand createCommand;

    @BeforeEach
    void setUp() {
        // Arrange
        createCommand = new CreateSensorReadingCommand(
                "CONTAINER-001",
                45,
                25.0,
                80
        );
    }

    @Test
    @DisplayName("Should create a sensor reading with default values and valid state")
    void shouldCreateSensorReadingWithValidDefaultValues() {
        // Act
        SensorReading reading = new SensorReading(createCommand);

        // Assert
        assertNotNull(reading);
        assertNotNull(reading.getRecordedAt());
        assertNotNull(reading.getReceivedAt());
        assertFalse(reading.getIsValidated());
        assertEquals(ValidationStatus.VALID, reading.getValidationStatus());
        assertEquals("CONTAINER-001", reading.getContainerId().value());
        assertEquals(45, reading.getFillLevel().percentage());
    }

    @Test
    @DisplayName("Should mark reading as valid when all values are within range")
    void shouldValidateAsValidWhenAllValuesAreInRange() {
        // Arrange
        SensorReading reading = new SensorReading(createCommand);

        // Act
        reading.validate();

        // Assert
        assertTrue(reading.getIsValidated());
        assertEquals(ValidationStatus.VALID, reading.getValidationStatus());
        assertFalse(reading.isAnomaly());
        assertFalse(reading.hasSensorError());
    }

    @Test
    @DisplayName("Should mark as anomaly when battery level is below 10 percent")
    void shouldMarkAsAnomalyWhenBatteryIsLow() {
        // Arrange
        CreateSensorReadingCommand lowBatteryCommand = new CreateSensorReadingCommand(
                "CONTAINER-001",
                45,
                25.0,
                5
        );
        SensorReading reading = new SensorReading(lowBatteryCommand);

        // Act
        reading.validate();

        // Assert
        assertTrue(reading.getIsValidated());
        assertEquals(ValidationStatus.ANOMALY, reading.getValidationStatus());
        assertTrue(reading.isAnomaly());
        assertTrue(reading.requiresMaintenance());
    }

    @Test
    @DisplayName("Should throw exception when creating command with invalid fill level")
    void shouldThrowExceptionWhenFillLevelIsInvalid() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CreateSensorReadingCommand(
                        "CONTAINER-001",
                        120,  // value beyond allowed range
                        25.0,
                        80
                )
        );

        assertEquals("Fill level percentage must be between 0 and 100", exception.getMessage());
    }
}