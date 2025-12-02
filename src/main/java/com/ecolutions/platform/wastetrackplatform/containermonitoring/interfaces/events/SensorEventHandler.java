package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.SensorReadingCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto.SensorBatchPayload;
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
            SensorBatchPayload batchEvent = objectMapper.readValue(payload, SensorBatchPayload.class);

            log.info("Cantidad detectada: {}", batchEvent.getCount());

            for (SensorReadingPayload readingDto : batchEvent.getReadings()) {

                log.info("Procesando dispositivo: {}", readingDto.getDeviceId());

                SensorReadingEvent evento = new SensorReadingEvent(
                        this,
                        readingDto.getDeviceId(),
                        readingDto.getContainerId(),
                        readingDto.getFillLevelPercentage(),
                        new DateTime(readingDto.getRecordedAt()),
                        new DateTime(readingDto.getReceivedAt()),
                        readingDto.getIsAlert(),
                        readingDto.getAlertType()
                );

                log.info( "Evento creado para sensor: {} en contenedor: {} con nivel de llenado: {}%",
                        evento.getDeviceId(),
                        evento.getContainerId(),
                        evento.getFillLevelPercentage()
                );

                var updateSensorReadingCommand = CreateSensorReadingCommandFromEvent.toCommandFromEvent(evento);
                sensorReadingCommandService.handle(updateSensorReadingCommand);
            }

        } catch (Exception e) {
            System.err.println("Error procesando batch de sensores: " + e.getMessage());
        }

    }


}