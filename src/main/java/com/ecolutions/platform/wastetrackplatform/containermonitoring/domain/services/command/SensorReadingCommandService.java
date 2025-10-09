package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteSensorReadingCommand;

import java.util.Optional;

public interface SensorReadingCommandService {
    Optional<SensorReading> handle(CreateSensorReadingCommand command);
    Optional<SensorReading> handle(UpdateSensorReadingCommand command);
    Boolean handle(DeleteSensorReadingCommand command);
}