package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.mappers;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateSensorReadingCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto.SensorReadingEvent;

public class CreateSensorReadingCommandFromEvent {
    public static CreateSensorReadingCommand toCommandFromEvent(SensorReadingEvent event) {
        return new CreateSensorReadingCommand(
            event.getContainerId(),
            event.getFillLevelPercentage(),
            5.0,
            100
        );
    }
}
