package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.stock.Stocks;

public abstract class Product {
    private static final String ERROR_NAME_REQUIRED = "[ERROR] 상품명은 필수입니다. 다시 입력하세요.";

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
            throw new IllegalArgumentException(ERROR_NAME_REQUIRED);
        }
    }

    public boolean hasEnoughStock(Quantity quantity) {
        if (isPromotionProduct()) {
            return stocks.hasEnoughPromotionStock(quantity);
        }
        return stocks.hasEnoughRegularStock(quantity);
    }

    public void removeStock(Quantity quantity) {
        stocks.removeStock(quantity, isPromotionProduct());
    }

    public Price calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.value());
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Stocks getStocks() {
        return stocks;
    }

    public abstract String promotionName();  // 추가된 추상 메서드

    public abstract boolean isPromotionProduct();
    public abstract PromotionType getPromotionType();  // 추가된 추상 메서드
}