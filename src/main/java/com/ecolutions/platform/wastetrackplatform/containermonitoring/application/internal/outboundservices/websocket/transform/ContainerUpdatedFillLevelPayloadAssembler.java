package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.outboundservices.websocket.transform;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.payloads.ContainerUpdatedFillLevelPayload;

public class ContainerUpdatedFillLevelPayloadAssembler {
    public static ContainerUpdatedFillLevelPayload toPayload(Container container) {
        return ContainerUpdatedFillLevelPayload.builder()
                .containerId(container.getId())
                .fillLevelPercentage(container.getCurrentFillLevel().percentage())
                .build();
    }
}
