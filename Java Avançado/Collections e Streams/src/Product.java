import java.math.BigDecimal;
import java.util.Objects;


public record Product(
        String name,
        Category category,
        BigDecimal price
    ) {

    public Product {
        Objects.requireNonNull(name,"name cannot be null");
        Objects.requireNonNull(category, "category cannot be null");
        Objects.requireNonNull(price, "price cannot be null");

        if (price.signum() < 0){
            throw new IllegalArgumentException("price cannot be negative");
        }
    }

}
