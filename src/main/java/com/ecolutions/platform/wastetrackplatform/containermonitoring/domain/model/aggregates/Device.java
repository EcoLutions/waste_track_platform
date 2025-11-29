package com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateDeviceCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.DeviceIdentifier;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Device extends AuditableAbstractAggregateRoot<Device> {
    @Embedded
    private DeviceIdentifier deviceIdentifier;

    public Device() {
        super();
    }

    public Device(CreateDeviceCommand command) {
        this();
        this.deviceIdentifier = DeviceIdentifier.of(command.deviceIdentifier());
    }
}
