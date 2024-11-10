package store.domain.product;

import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.stock.Stocks;

public class RegularProduct extends Product {

    public RegularProduct(String name, Price price, Stocks stocks) {
        super(name, price, stocks);
    }

    public static RegularProduct create(String name, Price price, Quantity regularQuantity, Quantity promotionQuantity) {
        return new RegularProduct(name, price, Stocks.of(regularQuantity, promotionQuantity));
    }

    @Override
    public boolean isPromotionProduct() {
        return false;
    }
}