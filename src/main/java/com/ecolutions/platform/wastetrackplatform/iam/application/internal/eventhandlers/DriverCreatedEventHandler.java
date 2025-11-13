package com.ecolutions.platform.wastetrackplatform.iam.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.CreateUserCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.entities.Role;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.events.DriverCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Roles;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("IamDriverCreatedEventHandler")
public class DriverCreatedEventHandler {
    private final UserCommandService userCommandService;

    public DriverCreatedEventHandler(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @EventListener(DriverCreatedEvent.class)
    @Async
    public void on(DriverCreatedEvent event) {
        var roleDriver = new Role(Roles.ROLE_DRIVER);

        var username = generateUsername(event.getFirstName(), event.getLastName());

        var command = new CreateUserCommand(
            event.getEmail(),
            username,
            List.of(roleDriver),
            event.getDistrictId()
        );
        userCommandService.handle(command);
    }

    private String generateUsername(String firstName, String lastName) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name cannot be null");
        }
        return firstName.toLowerCase() + "." + lastName.toLowerCase();
    }
}
