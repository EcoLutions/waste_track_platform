package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.queryservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllEvidencesQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetEvidenceByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries.EvidenceQueryService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.storage.StorageService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.EvidenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvidenceQueryServiceImpl implements EvidenceQueryService {

    private final EvidenceRepository evidenceRepository;
    private final StorageService storageService;

    @Override
    public Optional<Evidence> handle(GetEvidenceByIdQuery query) {
        try {
            return evidenceRepository.findById(query.id());
        } catch (Exception e) {
            log.error("Failed to retrieve evidence: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to retrieve evidence: " + e.getMessage());
        }
    }

    @Override
    public List<Evidence> handle(GetAllEvidencesQuery query) {
        try {
            return evidenceRepository.findAll();
        } catch (Exception e) {
            log.error("Failed to retrieve evidences: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to retrieve evidences: " + e.getMessage());
        }
    }

    // Método especial que genera URL temporal en tiempo real
    public String getEvidenceFileUrl(String evidenceId) {
        try {
            var evidence = evidenceRepository.findById(evidenceId);
            if (evidence.isEmpty()) {
                throw new IllegalArgumentException("Evidence with ID " + evidenceId + " not found");
            }

            return storageService.getFileUrl(evidence.get().getFilePath());

        } catch (IOException e) {
            log.error("Failed to generate file URL for evidence {}: {}", evidenceId, e.getMessage());
            throw new IllegalArgumentException("Failed to generate file URL: " + e.getMessage());
        }
    }
}