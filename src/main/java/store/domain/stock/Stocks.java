package store.domain.stock;

import store.domain.vo.Quantity;

public class Stocks {
    private final RegularStock regularStock;
    private final PromotionStock promotionStock;

    private Stocks(RegularStock regularStock, PromotionStock promotionStock) {
        this.regularStock = regularStock;
        this.promotionStock = promotionStock;
    }

    public static Stocks of(Quantity regularQuantity, Quantity promotionQuantity) {
        return new Stocks(
                new RegularStock(regularQuantity),
                new PromotionStock(promotionQuantity)
        );
    }

    public void removeStock(Quantity quantity, boolean isPromotion) {
        if (isPromotion) {
            removePromotionStock(quantity);
            return;
        }
        removeRegularStock(quantity);
    }

    public void removePromotionStock(Quantity quantity) {
        Quantity availablePromotion = promotionStock.getQuantity();
        if (availablePromotion.value() >= quantity.value()) {
            promotionStock.decrease(quantity);
            return;
        }

        promotionStock.decrease(availablePromotion);
        Quantity remaining = quantity.subtract(availablePromotion);
        if (remaining.value() > 0) {
            regularStock.decrease(remaining);
        }
    }

    public void removeRegularStock(Quantity quantity) {
        regularStock.decrease(quantity);
    }

    public boolean hasEnoughPromotionStock(Quantity quantity) {
        return promotionStock.canFulfillOrder(quantity);
    }

    public boolean hasEnoughRegularStock(Quantity quantity) {
        return regularStock.canFulfillOrder(quantity);
    }

    public Stock getStock(boolean isPromotion) {
        if (isPromotion) {
            return promotionStock;
        }
        return regularStock;
    }

    public RegularStock getRegularStock() {
        return regularStock;
    }

    public PromotionStock getPromotionStock() {
        return promotionStock;
    }
}
