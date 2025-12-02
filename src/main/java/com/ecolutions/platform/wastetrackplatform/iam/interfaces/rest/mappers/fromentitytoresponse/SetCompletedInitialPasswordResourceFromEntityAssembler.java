package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.SetCompletedInitialPasswordResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;

public class SetCompletedInitialPasswordResourceFromEntityAssembler {
    public static SetCompletedInitialPasswordResource toResourceFromEntity(User entity) {
        return SetCompletedInitialPasswordResource.builder()
                .userId(entity.getId())
                .email(EmailAddress.toStringOrNull(entity.getEmail()))
                .roles(RoleStringListFromEntityListAssembler.toResourceListFromEntitySet(entity.getRoles()))
                .build();
    }
}
