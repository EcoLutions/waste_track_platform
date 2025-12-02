package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.events.dto;

import com.google.api.client.util.DateTime;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SensorReadingEvent extends ApplicationEvent {
    private final String deviceId;
    private final String containerId;
    private final Integer fillLevelPercentage;
    private final DateTime recordedAt;
    private final DateTime receivedAt;
    private final Boolean isAlert;
    private final String alertType;

    public SensorReadingEvent(Object source, String deviceId, String containerId, Integer fillLevelPercentage,
                              DateTime recordedAt, DateTime receivedAt, Boolean isAlert, String alertType) {
        super(source);
        this.deviceId = deviceId;
        this.containerId = containerId;
        this.fillLevelPercentage = fillLevelPercentage;
        this.recordedAt = recordedAt;
        this.receivedAt = receivedAt;
        this.isAlert = isAlert;
        this.alertType = alertType;
    }


}
