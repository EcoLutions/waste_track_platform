package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.aggregates;

import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.InvoiceNumber;
import com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects.InvoiceStatus;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.DistrictId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.Money;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.PaymentId;
import com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects.SubscriptionId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Invoice extends AuditableAbstractAggregateRoot<Invoice> {
    @NotNull
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "subscription_id", nullable = false))
    private SubscriptionId subscriptionId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "district_id", nullable = false))
    private DistrictId districtId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "series", column = @Column(name = "invoice_number_series", nullable = false)),
        @AttributeOverride(name = "sequential", column = @Column(name = "invoice_number_sequential", nullable = false))
    })
    private InvoiceNumber invoiceNumber;

    @NotNull
    private LocalDate billingPeriodStart;

    @NotNull
    private LocalDate billingPeriodEnd;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "subtotal_amount", nullable = false, precision = 10, scale = 2)),
        @AttributeOverride(name = "currency", column = @Column(name = "subtotal_currency", nullable = false))
    })
    private Money subtotal;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "tax_amount_amount", nullable = false, precision = 10, scale = 2)),
        @AttributeOverride(name = "currency", column = @Column(name = "tax_amount_currency", nullable = false))
    })
    private Money taxAmount;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "total_amount_amount", nullable = false, precision = 10, scale = 2)),
        @AttributeOverride(name = "currency", column = @Column(name = "total_amount_currency", nullable = false))
    })
    private Money totalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private LocalDate issuedAt;

    @NotNull
    private LocalDate dueDate;

    private LocalDate paidAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "payment_id"))
    private PaymentId paymentId;

    public Invoice() {
        super();
        this.status = InvoiceStatus.DRAFT;
    }

    public Invoice(SubscriptionId subscriptionId, DistrictId districtId, InvoiceNumber invoiceNumber,
                  LocalDate billingPeriodStart, LocalDate billingPeriodEnd, Money subtotal,
                  Money taxAmount, Money totalAmount, LocalDate dueDate) {
        this();
        this.subscriptionId = subscriptionId;
        this.districtId = districtId;
        this.invoiceNumber = invoiceNumber;
        this.billingPeriodStart = billingPeriodStart;
        this.billingPeriodEnd = billingPeriodEnd;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.dueDate = dueDate;
    }

    public void issue() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Can only issue draft invoices");
        }
        this.status = InvoiceStatus.ISSUED;
        this.issuedAt = LocalDate.now();
    }

    public void markAsPaid(PaymentId paymentId) {
        if (this.status != InvoiceStatus.ISSUED) {
            throw new IllegalStateException("Can only mark issued invoices as paid");
        }
        this.status = InvoiceStatus.PAID;
        this.paymentId = paymentId;
        this.paidAt = LocalDate.now();
    }

    public void markAsOverdue() {
        if (this.status != InvoiceStatus.ISSUED) {
            throw new IllegalStateException("Can only mark issued invoices as overdue");
        }
        this.status = InvoiceStatus.OVERDUE;
    }

    public void voidInvoice() {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot void paid invoices");
        }
        this.status = InvoiceStatus.VOID;
    }

    public boolean isOverdue() {
        return this.status == InvoiceStatus.ISSUED && LocalDate.now().isAfter(this.dueDate);
    }

    public Integer getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(this.dueDate, LocalDate.now());
    }
}
