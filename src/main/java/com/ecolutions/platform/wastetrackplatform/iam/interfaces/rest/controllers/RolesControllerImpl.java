package com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.queries.GetAllRolesQuery;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.RoleQueryService;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.dto.response.RoleResource;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.mappers.fromentitytoresponse.RoleResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.iam.interfaces.rest.swagger.RolesController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RolesControllerImpl implements RolesController {
    private final RoleQueryService roleQueryService;

    @Override
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        var getAllRolesQuery = new GetAllRolesQuery();
        var roles = roleQueryService.handle(getAllRolesQuery);
        var roleResources = roles.stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(roleResources);
    }
}
