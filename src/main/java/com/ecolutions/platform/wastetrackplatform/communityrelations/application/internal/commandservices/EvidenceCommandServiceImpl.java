package com.ecolutions.platform.wastetrackplatform.communityrelations.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.commands.UploadEvidenceCommand;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.entities.Evidence;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.valueobjects.EvidenceType;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.EvidenceCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.storage.StorageService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.infrastructure.persistence.jpa.repositories.EvidenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvidenceCommandServiceImpl implements EvidenceCommandService {

    private final EvidenceRepository evidenceRepository;
    private final StorageService storageService;

    @Override
    public Optional<Evidence> handle(UploadEvidenceCommand command) {
        try {
            // 1. Subir archivo a Firebase (genera UUID único automáticamente)
            String filePath = storageService.uploadFile(command.file(), "evidence");

            // 2. Crear entidad Evidence (sin URLs persistidas)
            Evidence evidence = new Evidence(
                EvidenceType.fromMimeType(command.contentType()),
                filePath,
                command.originalFileName(),
                command.description(),
                command.fileSize(),
                command.contentType()
            );

            // 3. Guardar en BD
            var savedEvidence = evidenceRepository.save(evidence);
            log.info("Evidence uploaded successfully with ID: {}", savedEvidence.getId());

            return Optional.of(savedEvidence);

        } catch (IOException e) {
            log.error("Failed to upload evidence: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to upload evidence: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error uploading evidence: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to upload evidence: " + e.getMessage());
        }
    }
}