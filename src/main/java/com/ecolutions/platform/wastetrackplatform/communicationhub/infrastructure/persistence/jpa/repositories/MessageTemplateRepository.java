package com.ecolutions.platform.wastetrackplatform.communicationhub.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.communicationhub.domain.model.aggregates.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, String> {
    // Custom methods only when necessary
    Boolean existsByName(String name);
    List<MessageTemplate> findByCategory(String category);
    List<MessageTemplate> findByIsActive(Boolean isActive);
}