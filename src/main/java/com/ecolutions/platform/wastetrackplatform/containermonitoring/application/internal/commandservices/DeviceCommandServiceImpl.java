package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Device;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateDeviceCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.DeviceIdentifier;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.command.DeviceCommandService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceCommandServiceImpl implements DeviceCommandService {
    private final DeviceRepository deviceRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Optional<Device> handle(CreateDeviceCommand command) {
        if (deviceRepository.existsByDeviceIdentifier(new DeviceIdentifier(command.deviceIdentifier()))) {
            throw new IllegalArgumentException("Device with identifier " + command.deviceIdentifier() + " already exists.");
        }
        var device = new Device(command);
        var savedDevice = deviceRepository.save(device);

        var event = savedDevice.publishDeviceCreatedEvent();
        applicationEventPublisher.publishEvent(event);

        return Optional.of(savedDevice);
    }
}
