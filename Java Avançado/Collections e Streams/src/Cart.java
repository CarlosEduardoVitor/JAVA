import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cart {

    private final List<Product> products = new ArrayList<>();

    public void add(Product product){
        if (product == null) {
            throw new IllegalArgumentException("product cannot be null");
        }
        products.add(product);
    }

    public List<Product> products() {
        return Collections.unmodifiableList(products);
    }

    public BigDecimal total(){
        return products.stream()
                .map(Product::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalByCategory (Category category){
        return products.stream()
                .filter(product -> product.category() == category)
                .map(Product::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Category, BigDecimal> reportByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Product::price,
                                BigDecimal::add
                        )
                ));
    }

}
