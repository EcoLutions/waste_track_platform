package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {
    private Location location;
    private ContainerCapacity capacity;
    private DistrictId districtId;
    private CollectionFrequency frequency;
    private SensorId sensorId;

    @BeforeEach
    void setUp() {
        //  Arrange
        location = new Location(
                new BigDecimal("12.0464"),
                new BigDecimal("-77.0428")
        );
        capacity = new ContainerCapacity(1000, 500);
        districtId = new DistrictId("SURCO");
        frequency = new CollectionFrequency(3);
        sensorId = new SensorId("SENSOR-001");
    }

    @Test
    void shouldCreateContainerWithDefaultStatusAndEmptyFillLevel() {
        //  Arrange
        // (ya inicializado en setUp)

        //  Act
        Container container = new Container(location, capacity, ContainerType.ORGANIC, districtId, frequency, sensorId);

        //  Assert
        assertNotNull(container);
        assertEquals(ContainerStatus.ACTIVE, container.getStatus());
        assertEquals(0, container.getCurrentFillLevel().percentage());
        assertEquals(ContainerType.ORGANIC, container.getContainerType());
        assertEquals(sensorId, container.getSensorId());
    }

    @Test
    void shouldUpdateFillLevelAndTimestampCorrectly() {
        //  Arrange
        Container container = new Container(location, capacity, ContainerType.RECYCLABLE, districtId, frequency, sensorId);
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
        Container container = new Container(location, capacity, ContainerType.GENERAL, districtId, frequency, sensorId);
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
        Container container = new Container(location, capacity, ContainerType.ORGANIC, districtId, frequency, sensorId);

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
        Container container = new Container(location, capacity, ContainerType.RECYCLABLE, districtId, frequency, sensorId);
        container.updateFillLevel(new CurrentFillLevel(95), LocalDateTime.now());

        //  Act
        boolean result = container.isOverflowing();

        //  Assert
        assertTrue(result);
    }

    @Test
    void shouldChangeStatusCorrectly() {
        //  Arrange
        Container container = new Container(location, capacity, ContainerType.RECYCLABLE, districtId, frequency, sensorId);

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
        Container container = new Container(location, capacity, ContainerType.RECYCLABLE, districtId, frequency, sensorId);
        SensorId newSensor = new SensorId("SENSOR-NEW-123");

        //  Act
        container.assignSensor(newSensor);

        //  Assert
        assertEquals(newSensor, container.getSensorId());
    }
}
