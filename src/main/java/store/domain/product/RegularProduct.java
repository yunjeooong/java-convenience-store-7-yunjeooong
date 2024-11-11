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
        return "";  // 일반 상품은 빈 문자열 반환
    }

    @Override
    public PromotionType getPromotionType() {
        return null;  // 프로모션 타입은 null로 유지
    }

    @Override
    public boolean isPromotionProduct() {
        return false;
    }
}