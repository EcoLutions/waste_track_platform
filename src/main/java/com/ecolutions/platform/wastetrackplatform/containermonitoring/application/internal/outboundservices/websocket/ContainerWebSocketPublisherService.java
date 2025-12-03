package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.outboundservices.websocket;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.payloads.ContainerUpdatedFillLevelPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContainerWebSocketPublisherService {
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Publishes a message to the container fill level topic.
     * Destination: /topic/containers/{containerId}/fill-level
     */
    public void publishContainerUpdatedFillLevel(ContainerUpdatedFillLevelPayload payload) {
        String destination = "/topic/containers/" + payload.containerId() + "/fill-level";
        log.info("Publishing container fill level update to {}", destination);
        messagingTemplate.convertAndSend(destination, payload);
    }

}
