package com.ecolutions.platform.wastetrackplatform.routeplanningexecution.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.aggregates.Route;
import com.ecolutions.platform.wastetrackplatform.routeplanningexecution.domain.model.valueobjects.RouteStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DriverId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, String>, JpaSpecificationExecutor<Route> {
    @Query("SELECT r FROM Route r WHERE r.districtId = :districtId AND r.status IN :activeStatuses")
    List<Route> findActiveRoutesByDistrictId(@Param("districtId") DistrictId districtId, @Param("activeStatuses") List<RouteStatus> activeStatuses);
    List<Route> findByStatus(RouteStatus routeStatus);

    @Query("""
        SELECT r FROM Route r 
        WHERE r.driverId.value = :driverIdValue 
        AND r.status IN :activeStatuses
        AND r.scheduledStartAt < :endTime 
        AND r.scheduledEndAt > :startTime
    """)
    List<Route> findOverlappingRoutesForDriver(
            @Param("driverIdValue") String driverIdValue,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("activeStatuses") List<RouteStatus> activeStatuses
    );


    @Query("""
        SELECT r FROM Route r 
        WHERE r.vehicleId.value = :vehicleIdValue 
        AND r.status IN :activeStatuses
        AND r.scheduledStartAt < :endTime 
        AND r.scheduledEndAt > :startTime
    """)
    List<Route> findOverlappingRoutesForVehicle(
            @Param("vehicleIdValue") String vehicleIdValue,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("activeStatuses") List<RouteStatus> activeStatuses
    );
}