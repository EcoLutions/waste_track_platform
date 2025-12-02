package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorReadingPayload {
    private String deviceId;
    private String containerId;
    private Integer fillLevelPercentage;
    private String recordedAt;
    private String receivedAt;
    private Boolean isAlert;
    private String alertType;
}


