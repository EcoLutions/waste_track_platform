package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Vehicle;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateVehicleCommand;

import java.util.Optional;

public interface VehicleCommandService {
    Optional<Vehicle> handle(CreateVehicleCommand command);
    Optional<Vehicle> handle(UpdateVehicleCommand command);
    Boolean handle(DeleteVehicleCommand command);
}