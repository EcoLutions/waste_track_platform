package com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Device;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.DeviceIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    boolean existsByDeviceIdentifier(DeviceIdentifier deviceIdentifier);
    Optional<Device> findByDeviceIdentifier(DeviceIdentifier deviceIdentifier);
}
