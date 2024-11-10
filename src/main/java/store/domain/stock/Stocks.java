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
        return new Stocks(new RegularStock(regularQuantity), new PromotionStock(promotionQuantity));
    }

    public void decrease(Quantity quantity) {
        Quantity promotionQuantity = calculatePromotionQuantity(quantity);
        Quantity regularQuantity = quantity.subtract(promotionQuantity);

        promotionStock.decrease(promotionQuantity);
        regularStock.decrease(regularQuantity);
    }

    private Quantity calculatePromotionQuantity(Quantity requestedQuantity) {
        return promotionStock.calculateAvailableQuantity(requestedQuantity);
    }

    public boolean canFulfillOrder(Quantity quantity) {
        Quantity promotionQuantity = calculatePromotionQuantity(quantity);
        Quantity regularQuantity = quantity.subtract(promotionQuantity);

        return regularStock.canFulfillOrder(regularQuantity);
    }

    public Quantity availableRegularQuantity() {
        return regularStock.getQuantity();
    }

    public Quantity availablePromotionQuantity() {
        return promotionStock.getQuantity();
    }
}