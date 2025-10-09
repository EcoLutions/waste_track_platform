package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.mappers.fromentitytoresponse;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates.PaymentMethod;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.PaymentMethodType;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.interfaces.rest.dto.response.PaymentMethodResource;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.utils.DateTimeUtils;

public class PaymentMethodResourceFromEntityAssembler {
    public static PaymentMethodResource toResourceFromEntity(PaymentMethod entity) {
        return PaymentMethodResource.builder()
            .id(entity.getId())
            .districtId(DistrictId.toStringOrNull(entity.getDistrictId()))
            .type(PaymentMethodType.toStringOrNull(entity.getType()))
            .culqiTokenId(entity.getCulqiTokenId())
            .cardBrand(entity.getCardBrand())
            .lastFourDigits(entity.getLastFourDigits())
            .expiryMonth(entity.getExpiryMonth())
            .expiryYear(entity.getExpiryYear())
            .isDefault(entity.getIsDefault())
            .isValid(entity.getIsValid())
            .registeredAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getRegisteredAt()))
            .lastUsedAt(DateTimeUtils.localDateTimeToStringOrNull(entity.getLastUsedAt()))
            .build();
    }
}