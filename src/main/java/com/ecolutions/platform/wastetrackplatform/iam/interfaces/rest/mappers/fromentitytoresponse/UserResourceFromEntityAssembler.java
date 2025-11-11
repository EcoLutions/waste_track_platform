package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.aggregates.User;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.valueobjects.AccountStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Username;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.UserResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.EmailAddress;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        return UserResource.builder()
                .id(entity.getId())
                .email(EmailAddress.toStringOrNull(entity.getEmail()))
                .username(Username.toStringOrNull(entity.getUsername()))
                .status(AccountStatus.toStringOrNull(entity.getAccountStatus()))
                .failedLoginAttempts(entity.getFailedLoginAttempts())
                .lastLoginAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastLoginAt()))
                .passwordChangedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getPasswordChangedAt()))
                .createdAt(DateTimeUtils.dateToStringOrNull(entity.getCreatedAt()))
                .updatedAt(DateTimeUtils.dateToStringOrNull(entity.getUpdatedAt()))
                .roles(RoleStringListFromEntityListAssembler.toResourceListFromEntitySet(entity.getRoles()))
                .build();
    }
}
