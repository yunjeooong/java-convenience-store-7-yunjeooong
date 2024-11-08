package store.domain.discount;

import store.domain.cart.Cart;
import store.domain.vo.Price;

public interface DiscountPolicy {
    Price calculateDiscount(Cart cart);
    boolean isApplicable(Cart cart);
}
