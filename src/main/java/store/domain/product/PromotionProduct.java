package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.vo.Price;
import store.domain.vo.Quantity;
import store.domain.stock.Stocks;

public class PromotionProduct extends Product {
    private final PromotionType promotionType;

    private PromotionProduct(String name, Price price, Stocks stocks, PromotionType promotionType) {
        super(name, price, stocks);
        this.promotionType = promotionType;
    }

    public static PromotionProduct create(String name, Price price, Quantity regularStock,
                                          Quantity promotionStock, PromotionType promotionType) {
        return new PromotionProduct(
                name,
                price,
                Stocks.of(regularStock, promotionStock),
                promotionType
        );
    }

    @Override
    public String promotionName() {
        return promotionType.getName();
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    @Override
    public PromotionType getPromotionType() {
        return promotionType;
    }

    public boolean canApplyPromotion(Quantity quantity) {
        return promotionType.isApplicable(quantity);
    }

    public Quantity calculateFreeItems(Quantity quantity) {
        return promotionType.calculateFreeItems(quantity);
    }
}