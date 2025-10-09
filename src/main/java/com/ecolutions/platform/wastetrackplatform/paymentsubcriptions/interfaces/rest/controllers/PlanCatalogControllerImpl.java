package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeletePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllPlanCatalogsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPlanCatalogByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.PlanCatalogCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.PlanCatalogQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreatePlanCatalogResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdatePlanCatalogResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.PlanCatalogResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse.PlanCatalogResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand.CreatePlanCatalogCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger.PlanCatalogController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlanCatalogControllerImpl implements PlanCatalogController {
    private final PlanCatalogCommandService planCatalogCommandService;
    private final PlanCatalogQueryService planCatalogQueryService;

    @Override
    public ResponseEntity<PlanCatalogResource> createPlanCatalog(CreatePlanCatalogResource resource) {
        var command = CreatePlanCatalogCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdPlanCatalog = planCatalogCommandService.handle(command);
        if (createdPlanCatalog.isEmpty()) return ResponseEntity.badRequest().build();
        var planCatalogResource = PlanCatalogResourceFromEntityAssembler.toResourceFromEntity(createdPlanCatalog.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(planCatalogResource.id())
                .toUri();
        return ResponseEntity.created(location).body(planCatalogResource);
    }

    @Override
    public ResponseEntity<PlanCatalogResource> getPlanCatalogById(String id) {
        var query = new GetPlanCatalogByIdQuery(id);
        var planCatalog = planCatalogQueryService.handle(query);
        if (planCatalog.isEmpty()) return ResponseEntity.notFound().build();
        var planCatalogResource = PlanCatalogResourceFromEntityAssembler.toResourceFromEntity(planCatalog.get());
        return ResponseEntity.ok(planCatalogResource);
    }

    @Override
    public ResponseEntity<List<PlanCatalogResource>> getAllPlanCatalogs() {
        var query = new GetAllPlanCatalogsQuery();
        var planCatalogs = planCatalogQueryService.handle(query);
        var planCatalogResources = planCatalogs.stream()
                .map(PlanCatalogResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(planCatalogResources);
    }

    @Override
    public ResponseEntity<PlanCatalogResource> updatePlanCatalog(String id, UpdatePlanCatalogResource resource) {
        var command = new UpdatePlanCatalogCommand(id, resource.name(), resource.monthlyPriceAmount(),
                resource.monthlyPriceCurrency(), resource.maxVehicles(), resource.maxDrivers(), resource.maxContainers());
        var updatedPlanCatalog = planCatalogCommandService.handle(command);
        if (updatedPlanCatalog.isEmpty()) return ResponseEntity.badRequest().build();
        var planCatalogResource = PlanCatalogResourceFromEntityAssembler.toResourceFromEntity(updatedPlanCatalog.get());
        return ResponseEntity.ok(planCatalogResource);
    }

    @Override
    public ResponseEntity<Void> deletePlanCatalog(String id) {
        var command = new DeletePlanCatalogCommand(id);
        var deleted = planCatalogCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}