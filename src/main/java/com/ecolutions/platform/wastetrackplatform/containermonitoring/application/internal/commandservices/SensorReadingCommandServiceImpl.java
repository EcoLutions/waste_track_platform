package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.BatteryLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.CurrentFillLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.Temperature;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.SensorReadingCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.ContainerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorReadingCommandServiceImpl implements SensorReadingCommandService {
    private final SensorReadingRepository sensorReadingRepository;

    @Override
    public Optional<SensorReading> handle(CreateSensorReadingCommand command) {
        try {
            var containerId = ContainerId.of(command.containerId());
            var fillLevel = new CurrentFillLevel(command.fillLevelPercentage());
            var temperature = new Temperature(BigDecimal.valueOf(command.temperatureCelsius()));
            var batteryLevel = new BatteryLevel(command.batteryLevelPercentage());

            var newSensorReading = new SensorReading(containerId, fillLevel, temperature, batteryLevel);

            var savedSensorReading = sensorReadingRepository.save(newSensorReading);
            return Optional.of(savedSensorReading);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create sensor reading: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<SensorReading> handle(UpdateSensorReadingCommand command) {
        try {
            SensorReading existingSensorReading = sensorReadingRepository.findById(command.sensorReadingId())
                    .orElseThrow(() -> new IllegalArgumentException("SensorReading with ID " + command.sensorReadingId() + " not found."));

            if (command.fillLevelPercentage() != null) existingSensorReading.setFillLevel(new CurrentFillLevel(command.fillLevelPercentage()));
            if (command.temperatureCelsius() != null) existingSensorReading.setTemperature(new Temperature(BigDecimal.valueOf(command.temperatureCelsius())));
            if (command.batteryLevelPercentage() != null) existingSensorReading.setBatteryLevel(new BatteryLevel(command.batteryLevelPercentage()));

            var updatedSensorReading = sensorReadingRepository.save(existingSensorReading);
            return Optional.of(updatedSensorReading);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update sensor reading: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteSensorReadingCommand command) {
        try {
            var existingSensorReading = sensorReadingRepository.findById(command.sensorReadingId())
                .orElseThrow(() -> new IllegalArgumentException("SensorReading with ID " + command.sensorReadingId() + " not found."));
            sensorReadingRepository.delete(existingSensorReading);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete sensor reading: " + e.getMessage(), e);
        }
    }
}