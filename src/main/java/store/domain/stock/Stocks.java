package store.domain.stock;

import store.domain.vo.Quantity;

public class Stocks {
    private final Stock regularStock;
    private final Stock promotionStock;

    private Stocks(Stock regularStock, Stock promotionStock) {
        this.regularStock = regularStock;
        this.promotionStock = promotionStock;
    }

    public static Stocks of(Quantity regularQuantity, Quantity promotionQuantity) {
        return new Stocks(
                new RegularStock(regularQuantity),
                new PromotionStock(promotionQuantity)
        );
    }

    public boolean canFulfillOrder(Quantity quantity) {
        return regularStock.canFulfillOrder(quantity);
    }

    public boolean hasEnoughRegularStock(Quantity quantity) {
        return regularStock.canFulfillOrder(quantity);
    }

    public boolean hasEnoughPromotionStock(Quantity quantity) {
        return promotionStock.canFulfillOrder(quantity);
    }

    public Quantity availablePromotionQuantity() {
        return promotionStock.getQuantity();
    }

    public Stock getStock(boolean isPromotion) {
        if (isPromotion) {
            return promotionStock;
        }
        return regularStock;
    }

    // 다시 추가된 메서드
    public void decrease(Quantity quantity, boolean usePromotion) {
        if (usePromotion && hasEnoughPromotionStock(quantity)) {
            promotionStock.decrease(quantity);
            return;
        }
        regularStock.decrease(quantity);
    }
}