package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.SensorReadingCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto.SensorBatchPayload;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto.SensorFullPayload;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto.SensorReadingEvent;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto.SensorReadingPayload;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.mappers.CreateSensorReadingCommandFromEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.DateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventHandler {
    private final SensorReadingCommandService sensorReadingCommandService;
    private final ObjectMapper objectMapper;

    public void handleBatchEvent(String payload) {

        try {
            SensorBatchPayload batchPayload = objectMapper.readValue(payload, SensorBatchPayload.class);

            log.info("Cantidad detectada: {}", batchPayload.getCount());

            for (SensorReadingPayload readingDto : batchPayload.getReadings()) {

                log.info("Procesando dispositivo: {}", readingDto.getDeviceId());

                SensorReadingEvent readingEvent = new SensorReadingEvent(
                        this,
                        readingDto.getDeviceId(),
                        readingDto.getContainerId(),
                        readingDto.getFillLevelPercentage(),
                        new DateTime(readingDto.getRecordedAt()),
                        new DateTime(readingDto.getReceivedAt()),
                        readingDto.getIsAlert(),
                        readingDto.getAlertType()
                );

                log.info( "Lectura recibida de sensor: {} en contenedor: {} con nivel de llenado: {}%",
                        readingEvent.getDeviceId(),
                        readingEvent.getContainerId(),
                        readingEvent.getFillLevelPercentage()
                );

                var createSensorReadingCommand = CreateSensorReadingCommandFromEvent.toCommandFromEvent(readingEvent);
                sensorReadingCommandService.handle(createSensorReadingCommand);
            }

        } catch (Exception e) {
            System.err.println("Error procesando lote de lecturas de sensores: " + e.getMessage());
        }

    }

    public void handleAlertEvent(String payload) {
        try {
            SensorFullPayload fullPayload = objectMapper.readValue(payload, SensorFullPayload.class);

            log.info("Alerta detectada: {}", fullPayload.getAlertType());

            SensorReadingPayload alertPayload = fullPayload.getReading();

            log.info("Procesando alerta para dispositivo: {}", alertPayload.getDeviceId());

            SensorReadingEvent alertEvent = new SensorReadingEvent(
                    this,
                    alertPayload.getDeviceId(),
                    alertPayload.getContainerId(),
                    alertPayload.getFillLevelPercentage(),
                    new DateTime(alertPayload.getRecordedAt()),
                    new DateTime(alertPayload.getReceivedAt()),
                    alertPayload.getIsAlert(),
                    alertPayload.getAlertType()
            );

            var createSensorReadingCommand = CreateSensorReadingCommandFromEvent.toCommandFromEvent(alertEvent);
            sensorReadingCommandService.handle(createSensorReadingCommand);

        } catch (Exception e) {
            System.err.println("Error procesando alerta de sensor: " + e.getMessage());
        }
    }


}