package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.application.internal.commandservices;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.CreatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.DeletePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.commands.UpdatePlanCatalogCommand;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.entities.PlanCatalog;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.services.command.PlanCatalogCommandService;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories.PlanCatalogRepository;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanCatalogCommandServiceImpl implements PlanCatalogCommandService {
    private final PlanCatalogRepository planCatalogRepository;

    @Override
    public Optional<PlanCatalog> handle(CreatePlanCatalogCommand command) {
        try {
            var planCatalog = new PlanCatalog(command);
            var savedPlanCatalog = planCatalogRepository.save(planCatalog);
            return Optional.of(savedPlanCatalog);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create plan catalog: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PlanCatalog> handle(UpdatePlanCatalogCommand command) {
        var existingPlanCatalog = planCatalogRepository.findById(command.planCatalogId())
            .orElseThrow(() -> new IllegalArgumentException("PlanCatalog with ID " + command.planCatalogId() + " not found."));

        try {
            if (command.name() != null && !command.name().isBlank()) {
                existingPlanCatalog.setName(command.name());
            }
            if (command.monthlyPriceCurrency() != null && !command.monthlyPriceCurrency().isBlank() &&
                command.monthlyPriceAmount() != null && !command.monthlyPriceAmount().isBlank()) {
                var money = new Money(command.monthlyPriceCurrency(), new BigDecimal(command.monthlyPriceAmount()));
                existingPlanCatalog.setPrice(money);
            }
            if (command.maxVehicles() != null) {
                existingPlanCatalog.setMaxVehicles(command.maxVehicles());
            }

            if (command.maxDrivers() != null) {
                existingPlanCatalog.setMaxDrivers(command.maxDrivers());
            }
            if (command.maxContainers() != null) {
                existingPlanCatalog.setMaxContainers(command.maxContainers());
            }

            var updatedPlanCatalog = planCatalogRepository.save(existingPlanCatalog);
            return Optional.of(updatedPlanCatalog);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update plan catalog: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean handle(DeletePlanCatalogCommand command) {
        var existingPlanCatalog = planCatalogRepository.findById(command.planCatalogId())
            .orElseThrow(() -> new IllegalArgumentException("PlanCatalog with ID " + command.planCatalogId() + " not found."));
        try {
            planCatalogRepository.delete(existingPlanCatalog);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete plan catalog: " + e.getMessage(), e);
        }
    }
}