package store.domain.order;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Quantity;
import store.domain.vo.Money;

public class OrderLineItem {
    private final Product product;
    private final Quantity quantity;
    private final Money itemPrice;

    private OrderLineItem(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
        this.itemPrice = calculateItemPrice();
    }

    public static OrderLineItem create(Product product, Quantity quantity) {
        return new OrderLineItem(product, quantity);
    }

    public Money calculateItemPrice() {
        return product.calculateTotalPrice(quantity).toMoney();
    }

    public boolean hasPromotion() {
        return product.isPromotionProduct();
    }

    public OrderLineItem createFreeItemLine() {
        if (!canCreateFreeItem()) {
            return null;
        }
        return new OrderLineItem(product, calculateFreeQuantity());
    }

    private boolean canCreateFreeItem() {
        return hasPromotion() && calculateFreeQuantity().value() > 0;
    }

    public Quantity calculateFreeQuantity() {
        if (!hasPromotion()) {
            return Quantity.ZERO;
        }
        return ((PromotionProduct) product).calculateFreeItems(quantity);
    }
    public String productName() {
        return product.getName();
    }

    public int quantityValue() {
        return quantity.value();
    }

}