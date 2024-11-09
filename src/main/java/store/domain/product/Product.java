package store.domain.product;

import store.domain.vo.Price;
import store. domain.vo.Quantity;
import store.domain.stock.Stock;

public class Product {
    private final String name;
    private final Price price;
    private final Stock stock;

    public Product(String name, Price price, Stock stock) {
        validateName(name);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Product create(String name, Price price, Quantity quantity) {
        return new Product(name, price, new Stock(quantity));
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명은 필수입니다.");
        }
    }

    public boolean hasEnoughStock(Quantity quantity) {
        return stock.hasEnough(quantity);
    }

    public void removeStock(Quantity quantity) {
        stock.decrease(quantity);
    }

    public Price calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.value());
    }

    public boolean isPromotionProduct() {
        return false;
    }

    public String getName() {
        return name;
    }

    public Quantity getStockQuantity() {
        return stock.getQuantity();
    }
}