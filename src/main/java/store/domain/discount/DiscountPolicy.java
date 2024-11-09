package store.domain.discount;

import org.junit.jupiter.api.Order;
import store.domain.vo.Money;

public interface DiscountPolicy {
    Money calculateDiscount(Order order);
}
