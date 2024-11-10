package store.domain.product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import store.domain.vo.Quantity;

public class Products {
    private final List<RegularProduct> products;

    public Products(List<RegularProduct> products) {
        this.products = products;
    }

    public static Products from(List<RegularProduct> products) {
        return new Products(products);
    }


    public Optional<RegularProduct> findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    public boolean hasEnoughStock(String name, Quantity quantity) {
        return findByName(name)
                .map(product -> product.hasEnoughStock(quantity))
                .orElse(false);
    }

    public void removeStock(String name, Quantity quantity) {
        findByName(name).ifPresent(product -> product.removeStock(quantity));
    }

    public List<RegularProduct> getAllProducts() {
        return Collections.unmodifiableList(products);
    }
}
