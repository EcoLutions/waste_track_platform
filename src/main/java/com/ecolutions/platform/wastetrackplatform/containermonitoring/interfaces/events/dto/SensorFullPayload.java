package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorFullPayload {
    private String eventType;
    private String alertType;
    private SensorReadingPayload reading;
}