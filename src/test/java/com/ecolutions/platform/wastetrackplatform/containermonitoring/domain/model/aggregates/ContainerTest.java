package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {
    private CreateContainerCommand createCommand;
    private DeviceId deviceId;

    @BeforeEach
    void setUp() {
        createCommand = new CreateContainerCommand(
                "12.0464",
                "-77.0428",
                1000,
                90,
                "SENSOR-001",
                "ORGANIC",
                "SURCO",
                3
        );
        deviceId = DeviceId.of("SENSOR-001");
    }

    @Test
    void shouldCreateContainerWithDefaultStatusAndEmptyFillLevel() {
        //  Act
        Container container = new Container(createCommand, deviceId);

        //  Assert
        assertNotNull(container);
        assertEquals(ContainerStatus.ACTIVE, container.getStatus());
        assertEquals(0, container.getCurrentFillLevel().percentage());
        assertEquals(ContainerType.ORGANIC, container.getContainerType());
        assertEquals(deviceId, container.getDeviceId());
    }

    @Test
    void shouldUpdateFillLevelAndTimestampCorrectly() {
        //  Arrange
        CreateContainerCommand recyclableCommand = new CreateContainerCommand(
                "12.0464", "-77.0428", 1000, 90, "SENSOR-002",
                "RECYCLABLE", "SURCO", 3
        );
        Container container = new Container(recyclableCommand, DeviceId.of("SENSOR-002"));
        CurrentFillLevel newLevel = new CurrentFillLevel(85);
        LocalDateTime now = LocalDateTime.now();

        // Act
        container.updateFillLevel(newLevel, now);

        // Assert
        assertEquals(85, container.getCurrentFillLevel().percentage());
        assertEquals(now, container.getLastReadingTimestamp());
    }

    @Test
    void shouldResetFillLevelWhenMarkedAsCollected() {
        //  Arrange
        CreateContainerCommand generalCommand = new CreateContainerCommand(
                "12.0464", "-77.0428", 1000, 90, "SENSOR-003",
                "GENERAL", "SURCO", 3
        );
        Container container = new Container(generalCommand, DeviceId.of("SENSOR-003"));
        container.updateFillLevel(new CurrentFillLevel(95), LocalDateTime.now());
        LocalDateTime collectedAt = LocalDateTime.now();

        //  Act
        container.markAsCollected(collectedAt);

        // Assert
        assertEquals(0, container.getCurrentFillLevel().percentage());
        assertEquals(collectedAt, container.getLastCollectionDate());
    }

    @Test
    void shouldRequireCollectionWhenFillLevelIsHighOrFrequencyExceeded() {
        //  Arrange
        Container container = new Container(createCommand, deviceId);

        // Caso 1: porcentaje alto (>80)
        container.updateFillLevel(new CurrentFillLevel(85), LocalDateTime.now());
        assertTrue(container.requiresCollection());

        // Caso 2: tiempo excedido desde última recolección
        container.markAsCollected(LocalDateTime.now().minusDays(5));
        container.updateFillLevel(new CurrentFillLevel(50), LocalDateTime.now());
        assertTrue(container.requiresCollection());
    }

    @Test
    void shouldDetectOverflowWhenFillLevelAboveThreshold() {
        //  Arrange
        CreateContainerCommand recyclableCommand = new CreateContainerCommand(
                "12.0464", "-77.0428", 1000, 90, "SENSOR-004",
                "RECYCLABLE", "SURCO", 3
        );
        Container container = new Container(recyclableCommand, DeviceId.of("SENSOR-004"));
        container.updateFillLevel(new CurrentFillLevel(95), LocalDateTime.now());

        //  Act
        boolean result = container.isOverflowing();

        //  Assert
        assertTrue(result);
    }

    @Test
    void shouldChangeStatusCorrectly() {
        //  Arrange
        CreateContainerCommand recyclableCommand = new CreateContainerCommand(
                "12.0464", "-77.0428", 1000, 90, "SENSOR-005",
                "RECYCLABLE", "SURCO", 3
        );
        Container container = new Container(recyclableCommand, DeviceId.of("SENSOR-005"));

        //  Act & Assert
        container.scheduleMaintenanceDueToSensorFailure();
        assertEquals(ContainerStatus.MAINTENANCE, container.getStatus());

        container.activate();
        assertEquals(ContainerStatus.ACTIVE, container.getStatus());

        container.decommission();
        assertEquals(ContainerStatus.DECOMMISSIONED, container.getStatus());
    }

    @Test
    void shouldAssignNewSensorCorrectly() {
        //  Arrange
        CreateContainerCommand recyclableCommand = new CreateContainerCommand(
                "12.0464", "-77.0428", 1000, 90, "SENSOR-006",
                "RECYCLABLE", "SURCO", 3
        );
        Container container = new Container(recyclableCommand, DeviceId.of("SENSOR-006"));
        DeviceId newSensor = DeviceId.of("SENSOR-NEW-123");

        //  Act
        container.setDeviceId(newSensor);

        //  Assert
        assertEquals(newSensor, container.getDeviceId());
    }
}
