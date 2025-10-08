package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;

import java.util.List;
import java.util.Set;

public class RoleStringListFromEntityListAssembler {
    public static List<String> toResourceListFromEntitySet(Set<Role> entity) {
        return entity.stream()
                .map(Role::getStringName)
                .toList();
    }
}
