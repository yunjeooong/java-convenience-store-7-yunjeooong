package store.domain.discount;

import store.domain.order.Order;
import store.domain.vo.Money;

public class MembershipDiscountPolicy implements DiscountPolicy {
    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;
    @Override
    public Money calculateDiscount(Order order) {
        if (!order.hasMembership()) {
            return Money.ZERO;
        }
        Money discount = calculateMembershipDiscount(order);
        if (discount.value() > MAX_DISCOUNT) {
            return new Money(MAX_DISCOUNT);
        }
        return discount;
    }

    private Money calculateMembershipDiscount(Order order) {
        Money nonPromotionAmount = order.calculateNonPromotionAmount();
        return new Money((int) (nonPromotionAmount.value() * DISCOUNT_RATE));
    }
}
