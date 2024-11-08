package store.domain.cart;

import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class Cart {
    private final CartItems items;
    private final Products products;
    private boolean hasMembership;

    private Cart(Products products) {
        this.products = products;
        this.items = CartItems.create();
        this.hasMembership = false;
    }

    public static Cart create(Products products) {
        return new Cart(products);
    }

    public void addItem(String productName, Quantity quantity) {
        Product product = findProductOrThrow(productName);
        CartItem newItem = CartItem.create(product, quantity);
        items.addItem(newItem);
    }

    private Product findProductOrThrow(String productName) {
        return products.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("[ERROR] %s 상품을 찾을 수 없습니다.", productName)));
    }

    public Price calculateTotalPrice() {
        return items.calculateTotalPrice();
    }

    public void processOrder() {
        items.processOrder();
    }

    public void activateMembership() {
        this.hasMembership = true;
    }

    public boolean canApplyMembership() {
        return hasMembership && items.hasItems();
    }

    public boolean isEmpty() {
        return !items.hasItems();
    }
}
