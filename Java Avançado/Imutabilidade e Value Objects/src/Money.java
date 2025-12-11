import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) {

    public Money {
        Objects.requireNonNull(amount, "amount cannot be null"); // Serve para garantir que um valor não seja nulo antes de você usar ele
        Objects.requireNonNull(currency, "currency cannot be null"); // Serve para garantir que um valor não seja nulo antes de você usar ele

        if (amount.compareTo(BigDecimal.ZERO) < 0) { // O compareTo serve para comparar doid objetos e dizer qual é maior, menor ou igual
            throw new IllegalArgumentException("amount must be >= 0");
        }

        currency = currency.toUpperCase();
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match");
        }
    }
}
