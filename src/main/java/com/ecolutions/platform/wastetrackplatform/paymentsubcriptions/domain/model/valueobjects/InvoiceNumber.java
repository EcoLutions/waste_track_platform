package com.ecolutions.platform.wastetrackplatform.paymentsubcriptions.domain.model.valueobjects;

public record InvoiceNumber(String series, Integer sequential) {
    public InvoiceNumber {
        if (series == null || series.isBlank()) {
            throw new IllegalArgumentException("Invoice series cannot be null or blank");
        }
        if (sequential == null || sequential <= 0) {
            throw new IllegalArgumentException("Sequential number must be positive");
        }
    }

    public static String seriesOrNull(InvoiceNumber invoiceNumber) {
        return invoiceNumber != null ? invoiceNumber.series() : null;
    }

    public static Integer sequentialOrNull(InvoiceNumber invoiceNumber) {
        return invoiceNumber != null ? invoiceNumber.sequential() : null;
    }
}