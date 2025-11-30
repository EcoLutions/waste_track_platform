package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Device;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateDeviceCommand;

import java.util.Optional;

public interface DeviceCommandService {
    Optional<Device> handle(CreateDeviceCommand command);
}
