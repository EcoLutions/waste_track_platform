package com.ecolutions.platform.wastetrackplatform.iam.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events.DistrictCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Roles;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("IamDistrictCreatedEventHandler")
public class DistrictCreatedEventHandler {
    private final UserCommandService userCommandService;

    public DistrictCreatedEventHandler(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @EventListener(DistrictCreatedEvent.class)
    @Async
    public void on(DistrictCreatedEvent event) {
        var roleMunicipalAdministrator = new Role(Roles.ROLE_MUNICIPAL_ADMINISTRATOR);
        var command = new CreateUserCommand(event.getPrimaryAdminEmail(), List.of(roleMunicipalAdministrator), event.getDistrictId());
        userCommandService.handle(command);
    }
}
