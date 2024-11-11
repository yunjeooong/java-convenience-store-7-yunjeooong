package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.stock.Stocks;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class PromotionProduct extends Product {
    private final PromotionType promotionType;

    public PromotionProduct(String name, Price price, Stocks stocks, PromotionType promotionType) {
        super(name, price, stocks);
        this.promotionType = promotionType;
    }

    public static PromotionProduct create(String name, Price price,
                                          Quantity regularQuantity,
                                          Quantity promotionQuantity,
                                          PromotionType promotionType) {
        return new PromotionProduct(
                name,
                price,
                Stocks.of(regularQuantity, promotionQuantity),
                promotionType
        );
    }

    @Override
    public void removeStock(Quantity quantity) {
        stocks.removeStock(quantity, isPromotionProduct());
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    public boolean canApplyPromotion(Quantity quantity) {
        return promotionType.isApplicable(quantity) &&
                stocks.hasEnoughPromotionStock(quantity);
    }

    public Quantity calculateFreeItems(Quantity quantity) {
        if (!promotionType.isApplicable(quantity)) {
            return Quantity.ZERO;
        }
        return new Quantity(promotionType.calculateFreeItems(quantity));
    }

    public String promotionName() {
        return promotionType.getName();
    }
}