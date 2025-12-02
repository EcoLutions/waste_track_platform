package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.Subscription;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.SubscriptionStatus;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.SubscriptionResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PlanId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class SubscriptionResourceFromEntityAssembler {
    public static SubscriptionResource toResourceFromEntity(Subscription entity) {
        return SubscriptionResource.builder()
            .id(entity.getId())
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .planId(PlanId.toStringOrNull(entity.getPlanId()))
            .status(SubscriptionStatus.toStringOrNull(entity.getStatus()))
            .startDate(DateTimeUtils.localDateToStringOrNull(entity.getStartDate()))
            .trialEndDate(DateTimeUtils.localDateToStringOrNull(entity.getTrialEndDate()))
            .currentPeriodStart(DateTimeUtils.localDateToStringOrNull(entity.getCurrentPeriodStart()))
            .currentPeriodEnd(DateTimeUtils.localDateToStringOrNull(entity.getCurrentPeriodEnd()))
            .nextBillingDate(DateTimeUtils.localDateToStringOrNull(entity.getNextBillingDate()))
            .gracePeriodEndDate(DateTimeUtils.localDateToStringOrNull(entity.getGracePeriodEndDate()))
            .cancelledAt(DateTimeUtils.localDateToStringOrNull(entity.getCancelledAt()))
            .build();
    }
}