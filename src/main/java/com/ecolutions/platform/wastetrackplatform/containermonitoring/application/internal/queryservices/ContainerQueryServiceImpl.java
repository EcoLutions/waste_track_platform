package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainerByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainersInAlertByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.valueobjects.ContainerStatus;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries.ContainerQueryService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContainerQueryServiceImpl implements ContainerQueryService {
    private final ContainerRepository containerRepository;

    @Override
    public Optional<Container> handle(GetContainerByIdQuery query) {
        return containerRepository.findById(query.containerId());
    }

    @Override
    public List<Container> handle(GetAllContainersQuery query) {
        return containerRepository.findAll();
    }

    @Override
    public List<Container> handle(GetAllContainersByDistrictIdQuery query) {
        return containerRepository.findAllByDistrictId(new DistrictId(query.districtId()));
    }

    @Override
    public List<Container> handle(GetContainersInAlertByDistrictIdQuery query) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffDate = now.minusDays(3);
        LocalDateTime sensorCutoffDate = now.minusHours(2);
        return containerRepository.findContainersInAlert(new DistrictId(query.districtId()), ContainerStatus.MAINTENANCE, cutoffDate, sensorCutoffDate);
    }
}