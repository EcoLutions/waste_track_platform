package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UploadEvidenceCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateEvidenceResource;

public class UploadEvidenceCommandFromResourceAssembler {

    public static UploadEvidenceCommand toCommandFromResource(CreateEvidenceResource resource) {
        return new UploadEvidenceCommand(
            resource.description(),
            resource.file(),
            resource.file().getContentType(),
            resource.file().getOriginalFilename(),
            resource.file().getSize()
        );
    }
}