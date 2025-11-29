package com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ContainerStatus;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.SensorId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContainerRepository extends JpaRepository<Container, String> {
    List<Container> findAllByDistrictId(DistrictId districtId);

    @Query("""
        SELECT c FROM Container c WHERE
        c.districtId = :districtId AND (
        c.status = :maintenanceStatus OR
        c.currentFillLevel.percentage > 80 OR
        c.currentFillLevel.percentage > 90 OR
        c.lastCollectionDate < :cutoffDate OR
        c.lastReadingTimestamp < :sensorCutoffDate)
    """)
    List<Container> findContainersInAlert(DistrictId districtId, ContainerStatus maintenanceStatus, LocalDateTime cutoffDate, LocalDateTime sensorCutoffDate);
    boolean existsByDeviceId(DeviceId deviceId);
    boolean existsByLocation(Location location);
}
