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
        return sensorReadingRepository.findById(query.sensorReadingId());
    }

    @Override
    public List<SensorReading> handle(GetAllSensorReadingsQuery query) {
        return sensorReadingRepository.findAll();
    }
}