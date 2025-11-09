package com.ecolutions.platform.wastetrackplatform.containermonitoring.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.aggregates.Container;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainerByIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetAllContainersQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.model.queries.GetContainersByDistrictIdQuery;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.domain.services.queries.ContainerQueryService;
import com.ecolutions.platform.wastetrackplatform.containermonitoring.infrastructure.persistence.jpa.repositories.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContainerQueryServiceImpl implements ContainerQueryService {
    private final ContainerRepository containerRepository;

    @Override
    public Optional<Container> handle(GetContainerByIdQuery query) {
        try {
            return containerRepository.findById(query.containerId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve container: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Container> handle(GetAllContainersQuery query) {
        try {
            return containerRepository.findAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve containers: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Container> handle(GetContainersByDistrictIdQuery query) {
        try {
            return containerRepository.findByDistrictId_Value(query.districtId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve containers by district ID: " + e.getMessage(), e);
        }
    }
}