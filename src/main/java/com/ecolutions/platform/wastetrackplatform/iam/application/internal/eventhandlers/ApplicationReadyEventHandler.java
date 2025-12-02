package com.ecolutions.platform.wastetrackplatform.iam.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SeedRolesCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.commands.SeedSuperAdminCommand;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.RoleCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.services.UserCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ApplicationReadyEventHandler {
    private final RoleCommandService roleCommandService;
    private final UserCommandService userCommandService;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.username}")
    private String adminUsername;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

    public ApplicationReadyEventHandler(RoleCommandService roleCommandService, UserCommandService userCommandService) {
        this.roleCommandService = roleCommandService;
        this.userCommandService = userCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting to verify if roles seeding is needed for {} at {}", applicationName, currentTimestamp());
        var seedRolesCommand = new SeedRolesCommand();
        roleCommandService.handle(seedRolesCommand);
        LOGGER.info("Roles seeding verification finished for {} at {}", applicationName, currentTimestamp());

        LOGGER.info("Starting to verify if super admin seeding is needed for {} at {}", applicationName, currentTimestamp());
        var seedSuperAdminCommand = new SeedSuperAdminCommand(adminEmail, adminUsername, adminPassword);
        userCommandService.handle(seedSuperAdminCommand);
        LOGGER.info("Super admin seeding verification finished for {} at {}", applicationName, currentTimestamp());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
