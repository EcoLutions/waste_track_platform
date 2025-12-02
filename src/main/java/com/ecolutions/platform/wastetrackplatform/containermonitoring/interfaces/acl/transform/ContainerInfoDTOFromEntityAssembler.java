package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.transform;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;


public class ContainerInfoDTOFromEntityAssembler {

    public static ContainerInfoDTO toDtoFromEntity(Container container) {
        return new ContainerInfoDTO(
                container.getId(),
                container.getLocation(),
                container.getCurrentFillLevel().percentage(),
                container.getContainerType().name(),
                container.getStatus().name()
        );
    }
}
