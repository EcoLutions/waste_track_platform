package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.aggregates.Citizen;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.InitializeCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateCitizenCommand;

import java.util.Optional;

public interface CitizenCommandService {
    Optional<Citizen> handle(CreateCitizenCommand command);
    Optional<Citizen> handle(InitializeCitizenCommand command);
    Optional<Citizen> handle(UpdateCitizenCommand command);
    Boolean handle(DeleteCitizenCommand command);
}