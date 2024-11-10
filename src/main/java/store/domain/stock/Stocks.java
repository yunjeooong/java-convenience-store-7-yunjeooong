package store.domain.stock;

import store.domain.vo.Quantity;

import store.domain.vo.Quantity;

public class Stocks {
    private final Stock regularStock;
    private final Stock promotionStock;

    private Stocks(Stock regularStock, Stock promotionStock) {
        this.regularStock = regularStock;
        this.promotionStock = promotionStock;
    }

    public static Stocks of(Quantity regularQuantity, Quantity promotionQuantity) {
        return new Stocks(new Stock(regularQuantity), new Stock(promotionQuantity));
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

    // [수정] 일반 재고 수량 반환 메서드
    public Quantity availableRegularQuantity() {
        return regularStock.calculateAvailableQuantity(new Quantity(Integer.MAX_VALUE));
    }

    // [추가] 프로모션 재고 수량 반환 메서드
    public Quantity availablePromotionQuantity() {
        return promotionStock.calculateAvailableQuantity(new Quantity(Integer.MAX_VALUE));
    }
}