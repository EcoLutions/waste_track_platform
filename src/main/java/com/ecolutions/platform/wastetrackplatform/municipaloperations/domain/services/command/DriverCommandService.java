package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Driver;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDriverCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDriverCommand;

import java.util.Optional;

public interface DriverCommandService {
    Optional<Driver> handle(CreateDriverCommand command);
    Optional<Driver> handle(UpdateDriverCommand command);
    Boolean handle(DeleteDriverCommand command);
}