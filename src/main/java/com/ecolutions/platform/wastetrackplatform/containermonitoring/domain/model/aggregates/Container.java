package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Container extends AuditableAbstractAggregateRoot<Container> {
    @Embedded
    private Location location;

    @Embedded
    private ContainerCapacity capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContainerType containerType;

    @Embedded
    private CurrentFillLevel currentFillLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContainerStatus status;

    @Embedded
    @AttributeOverride(name = "sensorId", column = @Column(name = "sensor_id"))
    private SensorId sensorId;

    @Column(name = "last_reading_timestamp")
    private LocalDateTime lastReadingTimestamp;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id"))
    private DistrictId districtId;

    @Column(name = "last_collection_date")
    private LocalDateTime lastCollectionDate;

    @Embedded
    @AttributeOverride(name = "days", column = @Column(name = "collection_frequency"))
    private CollectionFrequency collectionFrequency;

    public Container() {
        super();
        this.status = ContainerStatus.ACTIVE;
        this.currentFillLevel = new CurrentFillLevel(0);
    }

    public Container(Location location, ContainerCapacity capacity,
                    ContainerType containerType, DistrictId districtId,
                    CollectionFrequency collectionFrequency) {
        this();
        this.location = location;
        this.capacity = capacity;
        this.containerType = containerType;
        this.districtId = districtId;
        this.collectionFrequency = collectionFrequency;
    }

    public void updateFillLevel(CurrentFillLevel newLevel, LocalDateTime timestamp) {
        this.currentFillLevel = newLevel;
        this.lastReadingTimestamp = timestamp;
    }

    public void markAsCollected(LocalDateTime collectedAt) {
        this.lastCollectionDate = collectedAt;
        this.currentFillLevel = new CurrentFillLevel(0);
    }

    public boolean requiresCollection() {
        if (lastCollectionDate == null) {
            return currentFillLevel.requiresCollection();
        }

        LocalDateTime nextCollectionDate = lastCollectionDate.plusDays(collectionFrequency.days());
        return LocalDateTime.now().isAfter(nextCollectionDate) || currentFillLevel.requiresCollection();
    }

    public boolean isOverflowing() {
        return currentFillLevel.isOverflowing();
    }

    public void assignSensor(SensorId sensorId) {
        this.sensorId = sensorId;
    }

    public void scheduleMaintenanceDueToSensorFailure() {
        this.status = ContainerStatus.MAINTENANCE;
    }

    public void activate() {
        this.status = ContainerStatus.ACTIVE;
    }

    public void decommission() {
        this.status = ContainerStatus.DECOMMISSIONED;
    }
}