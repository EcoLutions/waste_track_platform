package com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetAllEvidencesQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.model.queries.GetEvidenceByIdQuery;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.command.EvidenceCommandService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.queries.EvidenceQueryService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.domain.services.storage.StorageService;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.request.CreateEvidenceResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.dto.response.EvidenceResource;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromentitytoresponse.EvidenceResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.mappers.fromresourcetocommand.UploadEvidenceCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.communityrelations.interfaces.rest.swagger.EvidenceController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EvidenceControllerImpl implements EvidenceController {
    private final EvidenceCommandService evidenceCommandService;
    private final EvidenceQueryService evidenceQueryService;
    private final StorageService storageService;

    @Override
    public ResponseEntity<EvidenceResource> uploadEvidence(String description, MultipartFile file) {
        var command = UploadEvidenceCommandFromResourceAssembler.toCommandFromResource(new CreateEvidenceResource(description, file));
        var uploadedEvidence = evidenceCommandService.handle(command);
        if (uploadedEvidence.isEmpty()) return ResponseEntity.badRequest().build();
        var evidenceResource = EvidenceResourceFromEntityAssembler
                .toResourceFromEntity(uploadedEvidence.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(evidenceResource.id())
                .toUri();
        return ResponseEntity.created(location).body(evidenceResource);
    }

    @Override
    public ResponseEntity<EvidenceResource> getEvidenceById(String id) throws IOException {
        var query = new GetEvidenceByIdQuery(id);
        var evidence = evidenceQueryService.handle(query);
        if (evidence.isEmpty()) return ResponseEntity.notFound().build();
        String freshFileUrl = storageService.getFileUrl(evidence.get().getFilePath());
        var evidenceResource = EvidenceResourceFromEntityAssembler
                .toResourceWithUrl(evidence.get(), freshFileUrl);
        return ResponseEntity.ok(evidenceResource);
    }

    @Override
    public ResponseEntity<List<EvidenceResource>> getAllEvidences() {
        var query = new GetAllEvidencesQuery();
        var evidences = evidenceQueryService.handle(query);
        var evidenceResources = evidences.stream()
                .map(evidence -> {
                    try {
                        String freshUrl = storageService.getFileUrl(evidence.getFilePath());
                        return EvidenceResourceFromEntityAssembler.toResourceWithUrl(evidence, freshUrl);
                    } catch (IOException e) {
                        log.warn("Could not generate URL for evidence {}: {}", evidence.getId(), e.getMessage());
                        return EvidenceResourceFromEntityAssembler.toResourceFromEntity(evidence);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(evidenceResources);
    }
}