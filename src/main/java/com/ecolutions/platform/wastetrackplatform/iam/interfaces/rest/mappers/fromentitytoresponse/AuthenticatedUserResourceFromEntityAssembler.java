package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Username;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.AuthenticatedUserResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User entity, String token) {
        return AuthenticatedUserResource.builder()
                .id(entity.getId())
                .email(EmailAddress.toStringOrNull(entity.getEmail()))
                .username(Username.toStringOrNull(entity.getUsername()))
                .token(token)
                .build();
    }
}
