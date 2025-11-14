package com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.contexts;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;

import java.util.List;
import java.util.Optional;


public interface ContainerMonitoringContextFacade {
    Optional<ContainerInfoDTO> getContainerInfo(String containerId);
    List<ContainerInfoDTO> getContainersInfo(List<String> containerIds);
    List<ContainerInfoDTO> getContainersRequiringCollection(DistrictId districtId);
}
