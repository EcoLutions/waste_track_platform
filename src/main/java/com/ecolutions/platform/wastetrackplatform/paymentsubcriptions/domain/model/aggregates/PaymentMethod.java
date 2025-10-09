package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.PaymentMethodType;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PaymentMethod extends AuditableAbstractAggregateRoot<PaymentMethod> {
    @Embedded
    @AttributeOverride(name = "districtId", column = @Column(name = "district_id", nullable = false))
    private DistrictId districtId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethodType type;

    @NotNull
    private String culqiTokenId;

    private String cardBrand;

    private String lastFourDigits;

    private Integer expiryMonth;

    private Integer expiryYear;

    @NotNull
    private Boolean isDefault;

    @NotNull
    private Boolean isValid;

    @NotNull
    private LocalDateTime registeredAt;

    private LocalDateTime lastUsedAt;

    public PaymentMethod() {
        super();
        this.isDefault = false;
        this.isValid = true;
        this.registeredAt = LocalDateTime.now();
    }

    public PaymentMethod(DistrictId districtId, PaymentMethodType type, String culqiTokenId,
                        String cardBrand, String lastFourDigits, Integer expiryMonth, Integer expiryYear) {
        this();
        this.districtId = districtId;
        this.type = type;
        this.culqiTokenId = culqiTokenId;
        this.cardBrand = cardBrand;
        this.lastFourDigits = lastFourDigits;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public boolean isExpired() {
        if (this.expiryMonth == null || this.expiryYear == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = LocalDateTime.of(this.expiryYear, this.expiryMonth, 1, 23, 59, 59);

        return now.isAfter(expiryDate);
    }

    public void markAsUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void removeAsDefault() {
        this.isDefault = false;
    }

    public void invalidate() {
        this.isValid = false;
    }

    public void validate() {
        this.isValid = true;
    }
}
