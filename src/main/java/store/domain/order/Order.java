package store.domain.order;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import store.domain.vo.Money;
import java.util.List;

public class Order {
    private final List<OrderLineItem> orderItems;
    private final boolean hasMembership;
    private Money promotionDiscount;
    private Money membershipDiscount;

    private Order(List<OrderLineItem> orderItems, boolean hasMembership) {
        this.orderItems = orderItems;
        this.hasMembership = hasMembership;
        this.promotionDiscount = Money.ZERO;
        this.membershipDiscount = Money.ZERO;
    }

    public static Order create(List<OrderLineItem> orderItems, boolean hasMembership) {
        return new Order(orderItems, hasMembership);
    }

    public Money calculateTotalAmount() {
        return orderItems.stream()
                .map(OrderLineItem::calculateItemPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public List<OrderLineItem> getFreeItems() {
        return orderItems.stream()
                .map(OrderLineItem::createFreeItemLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void applyPromotionDiscount(Money discount) {
        this.promotionDiscount = discount;
    }

    public void applyMembershipDiscount(Money discount) {
        this.membershipDiscount = discount;
    }

    public Money getFinalAmount() {
        return calculateTotalAmount()
                .subtract(promotionDiscount)
                .subtract(membershipDiscount);
    }

    public boolean hasMembership() {
        return hasMembership;
    }

    public List<OrderLineItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public Money getPromotionDiscount() {
        return promotionDiscount;
    }

    public Money getMembershipDiscount() {
        return membershipDiscount;
    }
}