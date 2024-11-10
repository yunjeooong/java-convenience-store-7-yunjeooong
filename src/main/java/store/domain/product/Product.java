package store.domain.product;

import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.stock.Stocks;

public abstract class Product {
    private final String name;
    private final Price price;
    protected final Stocks stocks;

    protected Product(String name, Price price, Stocks stocks) {
        validateName(name);
        this.name = name;
        this.price = price;
        this.stocks = stocks;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명은 필수입니다.");
        }
    }

    public boolean hasEnoughStock(Quantity quantity) {
        return stocks.canFulfillOrder(quantity);
    }

    public void removeStock(Quantity quantity) {
        stocks.decrease(quantity);
    }

    public Price calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.value());
    }

    public String getName() {
        return name;
    }

    public abstract boolean isPromotionProduct();
}