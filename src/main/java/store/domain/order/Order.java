package store.domain.order;

import store.domain.vo.Money;
import java.util.List;

public class Order {
    private final List<OrderLineItem> orderItems;
    private final boolean hasMembership;
    private Money totalAmount;
    private Money discountAmount;

    private Order(List<OrderLineItem> orderItems, boolean hasMembership) {
        this.orderItems = orderItems;
        this.hasMembership = hasMembership;
        this.totalAmount = calculateTotalAmount();
        this.discountAmount = Money.ZERO;
    }

    public static Order create(List<OrderLineItem> orderItems, boolean hasMembership) {
        return new Order(orderItems, hasMembership);
    }

    public Money calculateTotalAmount() {
        return orderItems.stream()
                .map(OrderLineItem::calculateItemPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public boolean hasMembership() {
        return hasMembership;
    }

    public void applyDiscount(Money discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Money getFinalAmount() {
        return totalAmount.subtract(discountAmount);
    }

    public List<OrderLineItem> getOrderItems() {
        return orderItems;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }
}