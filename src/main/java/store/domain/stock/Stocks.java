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
                new Stock(regularQuantity),
                new Stock(promotionQuantity)
        );
    }

    public boolean hasEnoughRegularStock(Quantity quantity) {
        return regularStock.hasEnough(quantity);
    }

    public boolean hasEnoughPromotionStock(Quantity quantity) {
        return promotionStock.hasEnough(quantity);
    }

    public void decreaseRegularStock(Quantity quantity) {
        regularStock.decrease(quantity);
    }

    public void decreasePromotionStock(Quantity quantity) {
        promotionStock.decrease(quantity);
    }

    public Quantity getRegularQuantity() {
        return regularStock.getQuantity();
    }

    public Quantity getPromotionQuantity() {
        return promotionStock.getQuantity();
    }
}
