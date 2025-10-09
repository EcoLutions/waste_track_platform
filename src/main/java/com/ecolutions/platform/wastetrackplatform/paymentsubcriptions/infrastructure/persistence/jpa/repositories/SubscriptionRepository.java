package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.infrastructure.persistence.jpa.repositories;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.Subscription;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.SubscriptionStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    Boolean existsByDistrictId(DistrictId districtId);
    List<Subscription> findByDistrictId(DistrictId districtId);
    List<Subscription> findByStatus(SubscriptionStatus status);
}