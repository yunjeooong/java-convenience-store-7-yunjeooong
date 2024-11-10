package store.domain.stock;

import store.domain.vo.Quantity;

class Stock {
    private Quantity quantity;

    Stock(Quantity quantity) {
        this.quantity = quantity;
    }

    void decrease(Quantity amount) {
        validateDecrease(amount);
        quantity = quantity.subtract(amount);
    }

    private void validateDecrease(Quantity amount) {
        if (!canFulfillOrder(amount)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다.");
        }
    }

    boolean canFulfillOrder(Quantity required) {
        return !quantity.isLessThan(required);
    }

    Quantity calculateAvailableQuantity(Quantity requested) {
        if (quantity.isLessThan(requested)) {
            return quantity;
        }
        return requested;
    }
}