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

    public void applyDiscount(Order order) {
        discountPolicies.forEach(policy -> {
            if (policy instanceof PromotionDiscountPolicy) {
                order.applyPromotionDiscount(policy.calculateDiscount(order));
            }
            if (policy instanceof MembershipDiscountPolicy) {
                order.applyMembershipDiscount(policy.calculateDiscount(order));
            }
        });
    }
}