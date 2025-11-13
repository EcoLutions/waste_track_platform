package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.eventhandlers;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.InitializeCitizenCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.CitizenCommandService;
import com.ecolutions.platform.wastetrackplatform.iam.domain.model.events.SignedUpEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("CommunityRelationsSignedUpEventHandler")
public class SignedUpEventHandler {
    private final CitizenCommandService citizenCommandService;

    public SignedUpEventHandler(CitizenCommandService citizenCommandService) {
        this.citizenCommandService = citizenCommandService;
    }

    @EventListener(SignedUpEvent.class)
    @Async
    public void on(SignedUpEvent event) {
        var command = new InitializeCitizenCommand(event.getUserId());
        citizenCommandService.handle(command);
    }
}
