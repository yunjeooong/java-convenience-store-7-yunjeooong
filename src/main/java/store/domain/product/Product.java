package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.stock.Stocks;

public abstract class Product {
    private static final String ERROR_NAME_REQUIRED = "[ERROR] 상품명은 필수입니다. 다시 입력하세요.";
    private static final String ERROR_EXCEEDS_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

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
    public void validateStock(Quantity quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException(ERROR_EXCEEDS_STOCK);
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

    public abstract String promotionName();

    public abstract boolean isPromotionProduct();
    public abstract PromotionType getPromotionType();
}
