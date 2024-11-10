package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.stock.Stocks;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

// PromotionProduct.java
public class PromotionProduct extends Product {
    private final PromotionType promotionType;

    private PromotionProduct(String name, Price price, Stocks stocks, PromotionType promotionType) {
        super(name, price, stocks);
        this.promotionType = promotionType;
    }

    public static PromotionProduct create(String name, Price price,
                                          Quantity regularQuantity,
                                          Quantity promotionQuantity,
                                          PromotionType promotionType) {
        return new PromotionProduct(name, price,
                Stocks.of(regularQuantity, promotionQuantity),
                promotionType);
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    // 프로모션 적용 가능 여부 확인
    public boolean canApplyPromotion(Quantity quantity) {
        return promotionType.isApplicable(quantity) &&
                stocks.hasEnoughPromotionStock( calculateFreeItems(quantity));
    }

    // 무료 증정 수량 계산
    public Quantity calculateFreeItems(Quantity purchaseQuantity) {
        return promotionType.calculateFreeItems(purchaseQuantity);
    }

    // 프로모션 상품명 반환
    public String getPromotionName() {
        return promotionType.getName();
    }

    // 추가 구매 제안을 위한 필요 수량 계산
    public Quantity calculateRequiredAdditionalQuantity(Quantity currentQuantity) {
        return promotionType.calculateRequiredQuantity(currentQuantity);
    }

    // 프로모션 적용 불가능한 수량 계산
    public Quantity calculateNonPromotionQuantity(Quantity quantity) {
        Quantity availableQuantity = stocks.availablePromotionQuantity();
        if (availableQuantity.isLessThan(quantity)) {
            return quantity.subtract(availableQuantity);
        }
        return Quantity.ZERO;
    }
}