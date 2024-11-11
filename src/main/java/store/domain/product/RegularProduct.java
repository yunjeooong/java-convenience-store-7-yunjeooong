package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.stock.Stocks;

public class RegularProduct extends Product {
    private RegularProduct(String name, Price price, Stocks stocks) {
        super(name, price, stocks);
    }

    public static RegularProduct create(String name, Price price, Quantity regularStock, Quantity promotionStock) {
        return new RegularProduct(name, price, Stocks.of(regularStock, promotionStock));  // create -> of
    }
    @Override
    public String promotionName() {
        return "";
    }

    @Override
    public PromotionType getPromotionType() {
        return null;
    }

    @Override
    public boolean isPromotionProduct() {
        return false;
    }
}