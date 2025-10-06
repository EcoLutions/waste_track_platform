package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.District;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteDistrictCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateDistrictCommand;

import java.util.Optional;

public interface DistrictCommandService {
    Optional<District> handle(CreateDistrictCommand command);
    Optional<District> handle(UpdateDistrictCommand command);
    Boolean handle(DeleteDistrictCommand command);
}