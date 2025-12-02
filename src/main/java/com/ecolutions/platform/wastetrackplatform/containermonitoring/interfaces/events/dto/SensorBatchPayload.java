package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorBatchPayload {
    private String eventType;
    private Integer count;
    private List<SensorReadingPayload> readings;
}
