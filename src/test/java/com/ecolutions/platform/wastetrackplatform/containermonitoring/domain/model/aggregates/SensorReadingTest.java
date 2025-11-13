package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.BatteryLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.CurrentFillLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.Temperature;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ValidationStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SensorReadingTest {

/*    private ContainerId containerId;
    private CurrentFillLevel fillLevel;
    private Temperature temperature;
    private BatteryLevel batteryLevel;

    @BeforeEach
    void setUp() {
        // Arrange
        containerId = new ContainerId("CONTAINER-001");
        fillLevel = new CurrentFillLevel(45);
        temperature = new Temperature(BigDecimal.valueOf(25));
        batteryLevel = new BatteryLevel(80);
    }

    @Test
    @DisplayName("Should create a sensor reading with default values and valid state")
    void shouldCreateSensorReadingWithValidDefaultValues() {
        // Act
        SensorReading reading = new SensorReading(containerId, fillLevel, temperature, batteryLevel);

        // Assert
        assertNotNull(reading);
        assertNotNull(reading.getRecordedAt());
        assertNotNull(reading.getReceivedAt());
        assertFalse(reading.getIsValidated());
        assertEquals(ValidationStatus.VALID, reading.getValidationStatus());
        assertEquals(containerId, reading.getContainerId());
        assertEquals(fillLevel, reading.getFillLevel());
    }

    @Test
    @DisplayName("Should mark reading as valid when all values are within range")
    void shouldValidateAsValidWhenAllValuesAreInRange() {
        // Arrange
        SensorReading reading = new SensorReading(containerId, fillLevel, temperature, batteryLevel);

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
        BatteryLevel lowBattery = new BatteryLevel(5);
        SensorReading reading = new SensorReading(containerId, fillLevel, temperature, lowBattery);

        // Act
        reading.validate();

        // Assert
        assertTrue(reading.getIsValidated());
        assertEquals(ValidationStatus.ANOMALY, reading.getValidationStatus());
        assertTrue(reading.isAnomaly());
        assertTrue(reading.requiresMaintenance());
    }

    @Test
    @DisplayName("Should mark as sensor error when fill level is invalid")
    void shouldMarkAsSensorErrorWhenFillLevelIsInvalid() {
        // Arrange
        CurrentFillLevel invalidFill = new CurrentFillLevel(100);
        SensorReading reading = new SensorReading(containerId, invalidFill, temperature, batteryLevel);

        //
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CurrentFillLevel(120) // value beyond allowed range
        );

        assertEquals("Percentage must be between 0 and 100", exception.getMessage());
    }*/
}