package store.domain.discount;

import store.domain.order.Order;
import store.domain.vo.Money;
import java.util.List;

public class DiscountManager {
    private final List<DiscountPolicy> discountPolicies;

    private DiscountManager(List<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public static DiscountManager create(List<DiscountPolicy> policies) {
        return new DiscountManager(policies);
    }

    public Money calculateTotalDiscount(Order order) {
        return discountPolicies.stream()
                .map(policy -> policy.calculateDiscount(order))
                .reduce(Money.ZERO, Money::add);
    }
}