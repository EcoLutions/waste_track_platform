package com.ecolutions.platform.wastetrackplatform.shared.infrastructure.broker.mqtt;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.SensorEventHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WasteTrackEventSubscriber {
    private final ObjectMapper objectMapper;
    private final SensorEventHandler sensorEventHandler;

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void handleMessage(Message<String> message) {
        String payload = message.getPayload();
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);

        try {
            JsonNode rootNode = objectMapper.readTree(payload);

            if (rootNode.has("eventType")) {
                String eventType = rootNode.get("eventType").asText();

                switch (eventType) {
                    case "SENSOR_READING_BATCH":
                        sensorEventHandler.handleBatchEvent(payload);
                        break;

                    case "SENSOR_ALERT":
                        //TODO: Implementar manejo de alertas de sensor
                        break;

                    default:
                        System.out.println("Evento desconocido: " + eventType);
                }
            } else {
                System.out.println("Recibido mensaje sin eventType en: " + topic);
            }

        } catch (Exception e) {
            throw new MessagingException(message, "Error procesando mensaje MQTT", e);
        }
    }
}