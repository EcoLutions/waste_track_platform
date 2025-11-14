package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.acl;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.contexts.ContainerMonitoringContextFacade;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.dtos.ContainerInfoDTO;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.interfaces.acl.transform.ContainerInfoDTOFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContainerMonitoringContextFacadeImpl implements ContainerMonitoringContextFacade {
    private final ContainerRepository containerRepository;

    @Override
    public Optional<ContainerInfoDTO> getContainerInfo(String containerId) {
        log.debug("ACL: Getting container info for containerId={}", containerId);
        return containerRepository.findById(containerId)
                .map(ContainerInfoDTOFromEntityAssembler::toDtoFromEntity);
    }

    @Override
    public List<ContainerInfoDTO> getContainersInfo(List<String> containerIds) {
        log.debug("ACL: Getting container info for {} containers", containerIds.size());
        return containerRepository.findAllById(containerIds).stream()
                .map(ContainerInfoDTOFromEntityAssembler::toDtoFromEntity)
                .toList();
    }

    @Override
    public List<ContainerInfoDTO> getContainersRequiringCollection(DistrictId districtId) {
        log.debug("ACL: Getting containers requiring collection for districtId={}", districtId.value());
        List<Container> containers = containerRepository.findAllByDistrictId(districtId);
        return containers.stream()
                .filter(Container::requiresCollection)
                .map(ContainerInfoDTOFromEntityAssembler::toDtoFromEntity)
                .toList();
    }
}
