package store.domain.product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public static Products from(List<Product> products) {
        return new Products(products);
    }

    public Optional<Product> findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }
}