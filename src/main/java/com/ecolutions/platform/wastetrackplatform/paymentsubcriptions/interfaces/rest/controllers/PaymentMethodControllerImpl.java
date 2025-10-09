package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeletePaymentMethodCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllPaymentMethodsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetPaymentMethodByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.PaymentMethodCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.PaymentMethodQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreatePaymentMethodResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdatePaymentMethodResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.PaymentMethodResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand.CreatePaymentMethodCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse.PaymentMethodResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand.UpdatePaymentMethodCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger.PaymentMethodController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentMethodControllerImpl implements PaymentMethodController {
    private final PaymentMethodCommandService paymentMethodCommandService;
    private final PaymentMethodQueryService paymentMethodQueryService;

    @Override
    public ResponseEntity<PaymentMethodResource> createPaymentMethod(CreatePaymentMethodResource resource) {
        var command = CreatePaymentMethodCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdPaymentMethod = paymentMethodCommandService.handle(command);
        if (createdPaymentMethod.isEmpty()) return ResponseEntity.badRequest().build();
        var paymentMethodResource = PaymentMethodResourceFromEntityAssembler.toResourceFromEntity(createdPaymentMethod.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(paymentMethodResource.id())
                .toUri();
        return ResponseEntity.created(location).body(paymentMethodResource);
    }

    @Override
    public ResponseEntity<PaymentMethodResource> getPaymentMethodById(String id) {
        var query = new GetPaymentMethodByIdQuery(id);
        var paymentMethod = paymentMethodQueryService.handle(query);
        if (paymentMethod.isEmpty()) return ResponseEntity.notFound().build();
        var paymentMethodResource = PaymentMethodResourceFromEntityAssembler.toResourceFromEntity(paymentMethod.get());
        return ResponseEntity.ok(paymentMethodResource);
    }

    @Override
    public ResponseEntity<List<PaymentMethodResource>> getAllPaymentMethods() {
        var query = new GetAllPaymentMethodsQuery();
        var paymentMethods = paymentMethodQueryService.handle(query);
        var paymentMethodResources = paymentMethods.stream()
                .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(paymentMethodResources);
    }

    @Override
    public ResponseEntity<PaymentMethodResource> updatePaymentMethod(String id, UpdatePaymentMethodResource resource) {
        var command = UpdatePaymentMethodCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedPaymentMethod = paymentMethodCommandService.handle(command);
        if (updatedPaymentMethod.isEmpty()) return ResponseEntity.badRequest().build();
        var paymentMethodResource = PaymentMethodResourceFromEntityAssembler.toResourceFromEntity(updatedPaymentMethod.get());
        return ResponseEntity.ok(paymentMethodResource);
    }

    @Override
    public ResponseEntity<Void> deletePaymentMethod(String id) {
        var command = new DeletePaymentMethodCommand(id);
        var deleted = paymentMethodCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}