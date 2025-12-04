package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.acl;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.commands.CreateContainerCommand;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.CurrentFillLevel;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DeviceId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContainerMonitoringContextFacadeImplTest {

    @Mock
    private ContainerRepository containerRepository;

    @InjectMocks
    private ContainerMonitoringContextFacadeImpl facade;

    private Container container1;
    private Container container2;

    @BeforeEach
    void setUp() {
        CreateContainerCommand command1 = new CreateContainerCommand(
                "12.0464", "-77.0428", 1000, 90, "SENSOR-001",
                "ORGANIC", "DIST-001", 3
        );
        container1 = new Container(command1, DeviceId.of("SENSOR-001"));
        container1.setId("CONTAINER-001");

        CreateContainerCommand command2 = new CreateContainerCommand(
                "12.0465", "-77.0429", 1000, 90, "SENSOR-002",
                "RECYCLABLE", "DIST-001", 3
        );
        container2 = new Container(command2, DeviceId.of("SENSOR-002"));
        container2.setId("CONTAINER-002");
        container2.updateFillLevel(new CurrentFillLevel(95), LocalDateTime.now());
    }

    @Test
    void getContainerInfo() {
        when(containerRepository.findById("CONTAINER-001")).thenReturn(Optional.of(container1));

        Optional<ContainerInfoDTO> result = facade.getContainerInfo("CONTAINER-001");

        assertTrue(result.isPresent());
        assertEquals("CONTAINER-001", result.get().containerId());
        verify(containerRepository, times(1)).findById("CONTAINER-001");
    }

    @Test
    void getContainersInfo() {
        List<String> containerIds = Arrays.asList("CONTAINER-001", "CONTAINER-002");
        when(containerRepository.findAllById(containerIds)).thenReturn(Arrays.asList(container1, container2));

        List<ContainerInfoDTO> result = facade.getContainersInfo(containerIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(containerRepository, times(1)).findAllById(containerIds);
    }

    @Test
    void getContainersRequiringCollection() {
        DistrictId districtId = DistrictId.of("DIST-001");
        when(containerRepository.findAllByDistrictId(districtId)).thenReturn(Arrays.asList(container1, container2));

        List<ContainerInfoDTO> result = facade.getContainersRequiringCollection(districtId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(containerRepository, times(1)).findAllByDistrictId(districtId);
    }
}