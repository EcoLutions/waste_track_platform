package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllSensorReadingsQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetSensorReadingByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries.SensorReadingQueryService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorReadingQueryServiceImpl implements SensorReadingQueryService {
    private final SensorReadingRepository sensorReadingRepository;

    @Override
    public Optional<SensorReading> handle(GetSensorReadingByIdQuery query) {
        try {
            return sensorReadingRepository.findById(query.sensorReadingId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve sensor reading: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SensorReading> handle(GetAllSensorReadingsQuery query) {
        try {
            return sensorReadingRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve sensor readings: " + e.getMessage(), e);
        }
    }
}