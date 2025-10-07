package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.CreateEvidenceCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.DeleteEvidenceCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UpdateEvidenceCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;

import java.util.Optional;

public interface EvidenceCommandService {

    Optional<Evidence> handle(CreateEvidenceCommand command);

    Optional<Evidence> handle(UpdateEvidenceCommand command);

    Boolean handle(DeleteEvidenceCommand command);
}