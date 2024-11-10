package store.domain.product;

import store.domain.vo.Price;
import store. domain.vo.Quantity;
import store.domain.stock.Stocks;

public class RegularProduct {
    private final String name;
    private final Price price;
    protected final Stocks stocks;

    public RegularProduct(String name, Price price, Stocks stocks) {
        validateName(name);
        this.name = name;
        this.price = price;
        this.stocks = stocks;
    }

    public static RegularProduct create(String name, Price price, Quantity regularQuantity, Quantity promotionQuantity) {
        return new RegularProduct(name, price, Stocks.of(regularQuantity, promotionQuantity));
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

    public boolean isPromotionProduct() {
        return false;
    }

    public String getName() {
        return name;
    }

    public Quantity getStockQuantity() {
        return stocks.availableRegularQuantity();
    }
}