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
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
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
