package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.outboundservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events.ContainerFillLevelUpdatedEvent;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.events.DeviceCreatedEvent;
import com.ecolutions.platform.wastetrackplatform.shared.infrastructure.broker.mqtt.MqttConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class DeviceEventPublisherService {
    private final MqttConfig.MqttOutboundGateway mqttGateway;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener
    public void handleDeviceCreatedEvent(DeviceCreatedEvent event) {
        String topic = "cm/devices/events/created";
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing DeviceCreatedEvent to JSON", e);
        }
        mqttGateway.sendToMqtt(topic, payload);
    }

    @TransactionalEventListener
    public void handleContainerFillLevelUpdatedEvent(ContainerFillLevelUpdatedEvent event) {
        String topic = "cm/containers/events/config/updated";
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing ContainerFillLevelUpdatedEvent to JSON", e);
        }
        mqttGateway.sendToMqtt(topic, payload);
    }


}