package store.domain.product;

import store.domain.promotion.PromotionType;
import store.domain.stock.Stocks;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class PromotionProduct extends RegularProduct {
    private final PromotionType promotionType;

    private PromotionProduct(String name, Price price, Stocks stocks, PromotionType promotionType) {
        super(name, price, stocks);
        this.promotionType = promotionType;
    }

    public static PromotionProduct create(String name, Price price, Quantity regularQuantity, Quantity promotionQuantity, PromotionType promotionType) {
        return new PromotionProduct(name, price, Stocks.of(regularQuantity, promotionQuantity), promotionType);
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    public boolean canApplyPromotion(Quantity quantity) {
        return promotionType.isApplicable(quantity) && hasEnoughPromotionStock(quantity);
    }

    private boolean hasEnoughPromotionStock(Quantity quantity) {
        Quantity freeItems = calculateFreeItems(quantity);
        return stocks.canFulfillOrder(freeItems);
    }

    public Quantity calculateFreeItems(Quantity quantity) {
        if (!canApplyPromotion(quantity)) {
            return Quantity.ZERO;
        }
        return promotionType.calculateFreeItems(quantity);
    }

    public boolean canSuggestMoreItems(Quantity currentQuantity) {
        return !promotionType.isApplicable(currentQuantity) && hasEnoughPromotionStock(calculateRequiredAdditionalQuantity(currentQuantity));
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
            stocks.decrease(freeItems, true);
        }
    }

    public String getPromotionName() {
        return promotionType.getName();
    }

    public boolean hasInsufficientPromotionStock(Quantity quantity) {
        Quantity freeItems = promotionType.calculateFreeItems(quantity);
        return !stocks.canFulfillOrder(freeItems);
    }

    public Quantity calculateNonPromotionQuantity(Quantity quantity) {
        if (!hasInsufficientPromotionStock(quantity)) {
            return Quantity.ZERO;
        }

        Quantity availablePromotionQuantity = calculateAvailablePromotionQuantity(quantity);
        return quantity.subtract(availablePromotionQuantity);
    }

    public Quantity calculateAvailablePromotionQuantity(Quantity requestedQuantity) {
        int maxPromotionSets = stocks.availablePromotionQuantity().value() / promotionType.getRequiredQuantity();
        return new Quantity(maxPromotionSets * promotionType.getRequiredQuantity());
    }

    public int getRequiredQuantity() {
        return promotionType.getRequiredQuantity();
    }
}