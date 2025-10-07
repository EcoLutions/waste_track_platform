package com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UploadEvidenceCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;

import java.util.Optional;

public interface EvidenceCommandService {
    Optional<Evidence> handle(UploadEvidenceCommand command);
}