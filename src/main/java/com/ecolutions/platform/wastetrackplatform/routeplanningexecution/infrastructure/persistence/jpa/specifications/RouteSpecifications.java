package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.specifications;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RouteSpecifications {

    public static Specification<Route> hasDistrictId(String districtId) {
        return (root, query, cb) -> {
            if (districtId == null || districtId.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("districtId").get("value"), districtId);
        };
    }

    public static Specification<Route> hasDriverId(String driverId) {
        return (root, query, cb) -> {
            if (driverId == null || driverId.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("driverId").get("value"), driverId);
        };
    }

    public static Specification<Route> hasVehicleId(String vehicleId) {
        return (root, query, cb) -> {
            if (vehicleId == null || vehicleId.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("vehicleId").get("value"), vehicleId);
        };
    }

    public static Specification<Route> hasStatus(RouteStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Route> hasStatusIn(List<RouteStatus> statuses) {
        return (root, query, cb) -> {
            if (statuses == null || statuses.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("status").in(statuses);
        };
    }

    public static Specification<Route> withFilters(
            String districtId,
            String driverId,
            String vehicleId,
            RouteStatus status,
            List<RouteStatus> statuses
    ) {
        return Specification.allOf(
                hasDistrictId(districtId),
                hasDriverId(driverId),
                hasVehicleId(vehicleId),
                hasStatus(status),
                hasStatusIn(statuses)
        );
    }
}
