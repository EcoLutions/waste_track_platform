package com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.queries;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Vehicle;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllVehiclesByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetAllVehiclesQuery;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    Optional<Vehicle> handle(GetVehicleByIdQuery query);
    List<Vehicle> handle(GetAllVehiclesQuery query);
    List<Vehicle> handle(GetAllVehiclesByDistrictIdQuery query);
}