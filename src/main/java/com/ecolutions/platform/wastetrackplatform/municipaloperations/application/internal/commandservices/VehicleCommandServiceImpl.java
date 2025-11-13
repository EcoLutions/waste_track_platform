package com.ecolutions.platform.wastetrackplatform.municipaloperations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.aggregates.Vehicle;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.CreateVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.DeleteVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.commands.UpdateVehicleCommand;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.LicensePlate;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.VehicleType;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.VolumeCapacity;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.model.valueobjects.WeightCapacity;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.domain.services.command.VehicleCommandService;
import com.ecolutions.platform.wastetrackplatform.municipaloperations.infrastructure.persistence.jpa.repositories.VehicleRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleCommandServiceImpl implements VehicleCommandService {
    private final VehicleRepository vehicleRepository;

    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        try {
            var licensePlate = new LicensePlate(command.licensePlate());
            var vehicleType = VehicleType.fromString(command.vehicleType());
            var volumeCapacity = command.volumeCapacity() != null ?
                new VolumeCapacity(command.volumeCapacity()) : null;
            var weightCapacity = command.weightCapacity() != null ?
                new WeightCapacity(command.weightCapacity().intValue()) : null;
            var districtId = DistrictId.of(command.districtId());

            var vehicle = new Vehicle(licensePlate, vehicleType, volumeCapacity, weightCapacity, districtId);
            var savedVehicle = vehicleRepository.save(vehicle);
            return Optional.of(savedVehicle);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        try {
            var existingVehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle with ID " + command.vehicleId() + " not found."));

            if (command.licensePlate() != null) {
                existingVehicle.setLicensePlate(new LicensePlate(command.licensePlate()));
            }
            if (command.vehicleType() != null) {
                existingVehicle.setVehicleType(VehicleType.fromString(command.vehicleType()));
            }
            if (command.volumeCapacity() != null) {
                existingVehicle.setVolumeCapacity(new VolumeCapacity(command.volumeCapacity()));
            }
            if (command.weightCapacity() != null) {
                existingVehicle.setWeightCapacity(new WeightCapacity(command.weightCapacity().intValue()));
            }
            if (command.districtId() != null) {
                existingVehicle.setDistrictId(DistrictId.of(command.districtId()));
            }
            if (command.lastMaintenanceDate() != null) {
                var lastMaintenanceDate = DateTimeUtils.stringToLocalDateTimeOrNull(command.lastMaintenanceDate());
                existingVehicle.setLastMaintenanceDate(lastMaintenanceDate);
            }
            if (command.nextMaintenanceDate() != null) {
                var nextMaintenanceDate = DateTimeUtils.stringToLocalDateTimeOrNull(command.nextMaintenanceDate());
                existingVehicle.setNextMaintenanceDate(nextMaintenanceDate);
            }
            if (command.isActive() != null) {
                if (command.isActive()) {
                    existingVehicle.activate();
                } else {
                    existingVehicle.deactivate();
                }
            }

            var updatedVehicle = vehicleRepository.save(existingVehicle);
            return Optional.of(updatedVehicle);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeleteVehicleCommand command) {
        try {
            var existingVehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle with ID " + command.vehicleId() + " not found."));
            vehicleRepository.delete(existingVehicle);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete vehicle: " + e.getMessage(), e);
        }
    }
}