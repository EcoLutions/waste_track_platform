package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events.ContainerBecameCriticalEvent;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.*;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    @AttributeOverride(name = "deviceId", column = @Column(name = "device_id"))
    private DeviceId deviceId;

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

    public Container(CreateContainerCommand command) {
        this();
        this.location = new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude()));
        this.capacity = new ContainerCapacity(command.volumeLiters(), command.maxFillLevel());
        this.containerType = ContainerType.fromString(command.containerType());
        this.districtId = DistrictId.of(command.districtId());
        this.collectionFrequency = new CollectionFrequency(command.collectionFrequencyDays());
        this.deviceId = DeviceId.of(command.deviceId());
    }

    public void update(UpdateContainerCommand command) {
        if (command.latitude() != null && command.longitude() != null) {
            this.location = new Location(new BigDecimal(command.latitude()), new BigDecimal(command.longitude()));
        }
        if (command.volumeLiters() != null && command.maxFillLevel() != null) {
            this.capacity = new ContainerCapacity(command.volumeLiters(), command.maxFillLevel());
        }
        if (command.containerType() != null) {
            this.containerType = ContainerType.fromString(command.containerType());
        }
        if (command.status() != null) {
            this.status = ContainerStatus.fromString(command.status());
        }
        if (command.collectionFrequencyDays() != null) {
            this.collectionFrequency = new CollectionFrequency(command.collectionFrequencyDays());
            this.lastCollectionDate = null;
        }
        this.deviceId = DeviceId.of(command.deviceId());
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

    public void scheduleMaintenanceDueToSensorFailure() {
        this.status = ContainerStatus.MAINTENANCE;
    }

    public void activate() {
        this.status = ContainerStatus.ACTIVE;
    }

    public void decommission() {
        this.status = ContainerStatus.DECOMMISSIONED;
    }

    public boolean hasBecomeCritical(CurrentFillLevel previousLevel) {
        boolean wasCritical = previousLevel != null && previousLevel.percentage() >= 90;
        boolean isCritical = this.currentFillLevel.percentage() >= 90;
        return !wasCritical && isCritical;
    }

    public ContainerBecameCriticalEvent buildContainerBecameCriticalEvent() {
        return ContainerBecameCriticalEvent.builder()
                .source(this)
                .containerId(this.getId())
                .districtId(this.districtId.value())
                .location(this.location)
                .fillLevel(this.currentFillLevel.percentage())
                .build();
    }
}