package store.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Products {
    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = new ArrayList<>(products);
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
        return new ArrayList<>(products);
    }

    public void updateProduct(Product updatedProduct) {
        int index = findProductIndex(updatedProduct.getName());
        updateProductAtIndex(index, updatedProduct);
    }

    private int findProductIndex(String name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void updateProductAtIndex(int index, Product product) {
        if (index >= 0) {
            products.set(index, product);
        }
    }
}