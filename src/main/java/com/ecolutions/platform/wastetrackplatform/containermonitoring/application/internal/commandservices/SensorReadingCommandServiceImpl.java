package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.SensorReadingCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorReadingCommandServiceImpl implements SensorReadingCommandService {
    private final SensorReadingRepository sensorReadingRepository;

    @Override
    public Optional<SensorReading> handle(CreateSensorReadingCommand command) {
        var newSensorReading = new SensorReading(command);
        var savedSensorReading = sensorReadingRepository.save(newSensorReading);
        return Optional.of(savedSensorReading);
    }

    @Override
    public Optional<SensorReading> handle(UpdateSensorReadingCommand command) {
        SensorReading existingSensorReading = sensorReadingRepository.findById(command.sensorReadingId())
                .orElseThrow(() -> new IllegalArgumentException("SensorReading with ID " + command.sensorReadingId() + " not found."));
        existingSensorReading.update(command);
        var updatedSensorReading = sensorReadingRepository.save(existingSensorReading);
        return Optional.of(updatedSensorReading);
    }

    @Override
    public Boolean handle(DeleteSensorReadingCommand command) {
        var existingSensorReading = sensorReadingRepository.findById(command.sensorReadingId())
                .orElseThrow(() -> new IllegalArgumentException("SensorReading with ID " + command.sensorReadingId() + " not found."));
        sensorReadingRepository.delete(existingSensorReading);
        return true;
    }
}