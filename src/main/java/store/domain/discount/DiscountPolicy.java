package store.domain.discount;

import store.domain.vo.Money;
import store.domain.order.Order;

public interface DiscountPolicy {
    Money calculateDiscount(Order order);
}
