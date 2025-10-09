package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeleteSubscriptionCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllSubscriptionsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetSubscriptionByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.SubscriptionCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.SubscriptionQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.CreateSubscriptionResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.request.UpdateSubscriptionResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.SubscriptionResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse.SubscriptionResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand.CreateSubscriptionCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromresourcetocommand.UpdateSubscriptionCommandFromResourceAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger.SubscriptionController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionControllerImpl implements SubscriptionController {
    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    @Override
    public ResponseEntity<SubscriptionResource> createSubscription(CreateSubscriptionResource resource) {
        var command = CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdSubscription = subscriptionCommandService.handle(command);
        if (createdSubscription.isEmpty()) return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(createdSubscription.get());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(subscriptionResource.id())
                .toUri();
        return ResponseEntity.created(location).body(subscriptionResource);
    }

    @Override
    public ResponseEntity<SubscriptionResource> getSubscriptionById(String id) {
        var query = new GetSubscriptionByIdQuery(id);
        var subscription = subscriptionQueryService.handle(query);
        if (subscription.isEmpty()) return ResponseEntity.notFound().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get());
        return ResponseEntity.ok(subscriptionResource);
    }

    @Override
    public ResponseEntity<List<SubscriptionResource>> getAllSubscriptions() {
        var query = new GetAllSubscriptionsQuery();
        var subscriptions = subscriptionQueryService.handle(query);
        var subscriptionResources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(subscriptionResources);
    }

    @Override
    public ResponseEntity<SubscriptionResource> updateSubscription(String id, UpdateSubscriptionResource resource) {
        var command = UpdateSubscriptionCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedSubscription = subscriptionCommandService.handle(command);
        if (updatedSubscription.isEmpty()) return ResponseEntity.badRequest().build();
        var subscriptionResource = SubscriptionResourceFromEntityAssembler.toResourceFromEntity(updatedSubscription.get());
        return ResponseEntity.ok(subscriptionResource);
    }

    @Override
    public ResponseEntity<Void> deleteSubscription(String id) {
        var command = new DeleteSubscriptionCommand(id);
        var deleted = subscriptionCommandService.handle(command);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}