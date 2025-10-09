package com.ecolutions.platform.wastetrackplatform.shared.domain.model.valueobjects;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public record Money(
    String currency,
    BigDecimal amount
) {
    public Money {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
    }

    public static Money of(String currency, BigDecimal amount) {
        if (currency == null || currency.isBlank() || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            return null;
        }
        return new Money(currency, amount);
    }

    public static String currencyOrNull(Money money) {
        return money != null ? money.currency() : null;
    }

    public static BigDecimal amountOrNull(Money money) {
        return money != null ? money.amount() : null;
    }
}
