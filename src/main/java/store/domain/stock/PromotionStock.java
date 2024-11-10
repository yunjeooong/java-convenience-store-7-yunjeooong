package store.domain.stock;

import store.domain.vo.Quantity;

public class PromotionStock implements Stock {
    private Quantity quantity;

    public PromotionStock(Quantity quantity) {
        this.quantity = quantity;
    }

    @Override
    public void decrease(Quantity amount) {
        validateDecrease(amount);
        quantity = quantity.subtract(amount);
    }

    private void validateDecrease(Quantity amount) {
        if (!canFulfillOrder(amount)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    @Override
    public boolean canFulfillOrder(Quantity required) {
        return !quantity.isLessThan(required);
    }

    @Override
    public Quantity calculateAvailableQuantity(Quantity requested) {
        return quantity.isLessThan(requested) ? quantity : requested;
    }

    @Override
    public Quantity getQuantity() {
        return quantity;
    }
}