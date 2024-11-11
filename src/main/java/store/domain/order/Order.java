package store.domain.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.vo.Money;
import java.util.List;
import store.domain.vo.Quantity;

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


    public Money calculateNonPromotionAmount() {
        return calculateTotalAmount().subtract(promotionDiscount);
    }

    public Money calculatePromotionDiscount() {
        return getFreeItems().stream()
                .map(item -> {
                    Money singleItemPrice = new Money(
                            item.getProduct().calculateTotalPrice(new Quantity(1)).value()
                    );
                    return singleItemPrice;
                })
                .reduce(Money.ZERO, Money::add);
    }

    public boolean hasMembership() {
        return hasMembership;
    }

    public List<OrderLineItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }
    public void removeStocks() {
        orderItems.forEach(OrderLineItem::removeStock);
    }

    public Money getPromotionDiscount() {
        return promotionDiscount;
    }

    public Money getMembershipDiscount() {
        return membershipDiscount;
    }
    public void addLineItem(Product product, Quantity quantity) {
        orderItems.add(OrderLineItem.create(product, quantity));
    }

    public static Order create() {
        return new Order(new ArrayList<>(), false);
    }

    public List<OrderLineItem> getLineItems() {
        return Collections.unmodifiableList(orderItems);
    }

}