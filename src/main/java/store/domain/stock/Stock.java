package store.domain.stock;

import store.domain.vo.Quantity;

public interface Stock {
    void decrease(Quantity amount);
    boolean canFulfillOrder(Quantity required);
    Quantity calculateAvailableQuantity(Quantity requested);
    Quantity getQuantity();
}