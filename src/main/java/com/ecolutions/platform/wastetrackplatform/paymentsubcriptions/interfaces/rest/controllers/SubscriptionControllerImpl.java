package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.controllers;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetAllSubscriptionsQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.queries.GetSubscriptionByIdQuery;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.queries.SubscriptionQueryService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.SubscriptionResource;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse.SubscriptionResourceFromEntityAssembler;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.swagger.SubscriptionController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionControllerImpl implements SubscriptionController {
    private final SubscriptionQueryService subscriptionQueryService;

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
}