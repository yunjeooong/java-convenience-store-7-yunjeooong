package store.domain.stock;

import store.domain.vo.Quantity;

public class Stock {
    private Quantity quantity;

    public Stock(Quantity quantity) {
        this.quantity = quantity;
    }

    public boolean hasEnough(Quantity required) {
        return !quantity.isLessThan(required);
    }

    public void decrease(Quantity amount) {
        if (!hasEnough(amount)) {
            throw new IllegalArgumentException("[ERROR] 재고가 부족합니다.");
        }
        this.quantity = this.quantity.subtract(amount);
    }

    public boolean isEmpty() {
        return quantity.equals(Quantity.ZERO);
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
