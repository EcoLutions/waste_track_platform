package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Device;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllDevicesQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetDeviceByIdQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {
    Optional<Device> handle(GetDeviceByIdQuery query);
    List<Device> handle(GetAllDevicesQuery query);
}
