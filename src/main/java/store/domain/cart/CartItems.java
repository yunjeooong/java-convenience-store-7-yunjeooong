package store.domain.cart;

import java.util.ArrayList;
import java.util.List;
import store.domain.vo.Price;
import store.domain.cart.CartItem;

public class CartItems {
    private final List<CartItem> items;

    private CartItems() {
        this.items = new ArrayList<>();
    }

    public static CartItems create() {
        return new CartItems();
    }

    public void addItem(CartItem item) {
        validateItem(item);
        item.validateStock();
        items.add(item);
    }

    private void validateItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("[ERROR] 장바구니 아이템은 필수입니다.");
        }
    }

    public Price calculateTotalPrice() {
        return items.stream()
                .map(CartItem::calculatePrice)
                .reduce(Price.ZERO, Price::add);
    }

    public void processOrder() {
        items.forEach(CartItem::processOrder);
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }
}