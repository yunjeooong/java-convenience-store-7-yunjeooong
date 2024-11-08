package store.domain.discount;

import store.domain.vo.Price;

public interface DiscountPolicy {
    Price calculate(Price price);
}
