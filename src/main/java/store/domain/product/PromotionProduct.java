package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.stock.Stock;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class PromotionProduct extends Product {
    private final PromotionType promotionType;
    private final Stock promotionStock;

    private PromotionProduct(String name, Price price,
                             Stock regularStock, Stock promotionStock,
                             PromotionType promotionType) {
        super(name, price, regularStock);
        this.promotionType = promotionType;
        this.promotionStock = promotionStock;
    }

    public static PromotionProduct create(String name, Price price,
                                          Quantity regularQuantity,
                                          Quantity promotionQuantity,
                                          PromotionType promotionType) {
        return new PromotionProduct(
                name, price,
                new Stock(regularQuantity),
                new Stock(promotionQuantity),
                promotionType
        );
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    public boolean canApplyPromotion(Quantity quantity) {
        return promotionType.isApplicable(quantity) &&
                hasEnoughPromotionStock(quantity);
    }

    private boolean hasEnoughPromotionStock(Quantity quantity) {
        Quantity freeItems = calculateFreeItems(quantity);
        return promotionStock.hasEnough(freeItems);
    }

    public Quantity calculateFreeItems(Quantity quantity) {
        if (!canApplyPromotion(quantity)) {
            return Quantity.ZERO;
        }
        return promotionType.calculateFreeItems(quantity);
    }

    public boolean canSuggestMoreItems(Quantity currentQuantity) {
        return !promotionType.isApplicable(currentQuantity) &&
                hasEnoughPromotionStock(calculateRequiredAdditionalQuantity(currentQuantity));
    }

    public Quantity calculateRequiredAdditionalQuantity(Quantity currentQuantity) {
        return promotionType.calculateRequiredQuantity(currentQuantity);
    }

    @Override
    public void removeStock(Quantity quantity) {
        super.removeStock(quantity);
        removePromotionStock(quantity);
    }

    private void removePromotionStock(Quantity quantity) {
        if (canApplyPromotion(quantity)) {
            Quantity freeItems = calculateFreeItems(quantity);
            promotionStock.decrease(freeItems);
        }
    }

    public String getPromotionName() {
        return promotionType.getName();
    }

    // 프로모션 재고 부족 여부 확인
    public boolean hasInsufficientPromotionStock(Quantity quantity) {
        Quantity freeItems = promotionType.calculateFreeItems(quantity);
        return !promotionStock.hasEnough(freeItems);
    }

    // 일반가로 구매해야 하는 수량 계산
    public Quantity calculateNonPromotionQuantity(Quantity quantity) {
        if (!hasInsufficientPromotionStock(quantity)) {
            return Quantity.ZERO;
        }

        Quantity availablePromotionQuantity =
                calculateAvailablePromotionQuantity(quantity);
        return quantity.subtract(availablePromotionQuantity);
    }

    //  프로모션 적용 가능한 최대 수량 계산
    public Quantity calculateAvailablePromotionQuantity(Quantity requestedQuantity) {
        int maxPromotionSets = promotionStock.getQuantity().value() /
                promotionType.getRequiredQuantity();
        return new Quantity(maxPromotionSets * promotionType.getRequiredQuantity());
    }

    // PromotionType에도 추가 필요한 메서드
    public int getRequiredQuantity() {
        return promotionType.getRequiredQuantity();
    }
}