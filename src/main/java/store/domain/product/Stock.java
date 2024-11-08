package store.domain.product;

import store.domain.vo.Quantity;

public class Stock {
    private Quantity quantity;

    public Stock(Quantity quantity) {
        this.quantity = quantity;
    }

    public boolean hasEnough(Quantity required) {
        return quantity.value() >= required.value();
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
