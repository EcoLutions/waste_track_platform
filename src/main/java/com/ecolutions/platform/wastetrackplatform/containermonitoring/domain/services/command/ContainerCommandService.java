package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.EmptyContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.UpdateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.DeleteContainerCommand;

import java.util.Optional;

public interface ContainerCommandService {
    Optional<Container> handle(CreateContainerCommand command);
    Optional<Container> handle(UpdateContainerCommand command);
    Boolean handle(DeleteContainerCommand command);
    Optional<Container> handle(EmptyContainerCommand command);
}