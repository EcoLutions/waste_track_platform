package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.SensorReading;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetSensorReadingByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllSensorReadingsQuery;

import java.util.List;
import java.util.Optional;

public interface SensorReadingQueryService {
    Optional<SensorReading> handle(GetSensorReadingByIdQuery query);
    List<SensorReading> handle(GetAllSensorReadingsQuery query);
}