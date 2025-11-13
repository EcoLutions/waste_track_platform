package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.BatteryLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.CurrentFillLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.Temperature;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ValidationStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SensorReading extends AuditableAbstractAggregateRoot<SensorReading> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "container_id"))
    private ContainerId containerId;

    @Embedded
    @AttributeOverride(name = "percentage", column = @Column(name = "fill_level_percentage"))
    private CurrentFillLevel fillLevel;

    @Embedded
    private Temperature temperature;

    @Embedded
    @AttributeOverride(name = "percentage", column = @Column(name = "battery_level_percentage"))
    private BatteryLevel batteryLevel;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "is_validated", nullable = false)
    private Boolean isValidated = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_status", nullable = false)
    private ValidationStatus validationStatus = ValidationStatus.VALID;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    public SensorReading() {
        super();
        this.recordedAt = LocalDateTime.now();
        this.receivedAt = LocalDateTime.now();
        this.isValidated = false;
    }

    public SensorReading(CreateSensorReadingCommand command) {
        this();
        this.containerId = ContainerId.of(command.containerId());
        this.fillLevel = new CurrentFillLevel(command.fillLevelPercentage());
        this.temperature = new Temperature(BigDecimal.valueOf(command.temperatureCelsius()));
        this.batteryLevel = new BatteryLevel(command.batteryLevelPercentage());
    }

    public void update(UpdateSensorReadingCommand command) {
        if (command.fillLevelPercentage() != null) this.fillLevel = new CurrentFillLevel(command.fillLevelPercentage());
        if (command.temperatureCelsius() != null) this.temperature = new Temperature(BigDecimal.valueOf(command.temperatureCelsius()));
        if (command.batteryLevelPercentage() != null) this.batteryLevel = new BatteryLevel(command.batteryLevelPercentage());
    }

    public void validate() {
        this.isValidated = true;
        if (fillLevel.percentage() < 0 || fillLevel.percentage() > 100) {
            this.validationStatus = ValidationStatus.SENSOR_ERROR;
        } else if (batteryLevel.requiresReplacement()) {
            this.validationStatus = ValidationStatus.ANOMALY;
        } else if (temperature.celsius().compareTo(BigDecimal.valueOf(-30)) < 0 ||
                   temperature.celsius().compareTo(BigDecimal.valueOf(80)) > 0) {
            this.validationStatus = ValidationStatus.ANOMALY;
        } else {
            this.validationStatus = ValidationStatus.VALID;
        }
    }

    public boolean isAnomaly() {
        return validationStatus == ValidationStatus.ANOMALY;
    }

    public boolean hasSensorError() {
        return validationStatus == ValidationStatus.SENSOR_ERROR;
    }

    public boolean requiresMaintenance() {
        return batteryLevel.requiresReplacement() || hasSensorError();
    }
}