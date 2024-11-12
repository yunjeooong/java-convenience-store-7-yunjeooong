package store.domain.discount;

import store.domain.order.Order;
import store.domain.vo.Money;
import store.domain.order.OrderLineItem;

public class PromotionDiscountPolicy implements DiscountPolicy {
    @Override
    public Money calculateDiscount(Order order) {
        Money totalDiscount = Money.ZERO;
        for (OrderLineItem item : order.getOrderItems()) {
            if (item.hasPromotion()) {
                Money individualPrice = item.getPrice().toMoney();
                Money discount = individualPrice.multiply(item.calculateFreeQuantity().value());
                totalDiscount = totalDiscount.add(discount);
            }
        }
        return totalDiscount;
    }
}