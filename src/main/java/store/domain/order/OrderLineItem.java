package store.domain.order;


import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class OrderLineItem {
    private final Product product;
    private final Quantity quantity;
    private final Price itemPrice;

    private OrderLineItem(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
        this.itemPrice = calculateItemPrice();
    }

    public static OrderLineItem create(Product product, Quantity quantity) {
        return new OrderLineItem(product, quantity);
    }

    private Price calculateItemPrice() {
        return product.calculateTotalPrice(quantity);
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

    private Quantity calculateFreeQuantity() {
        if (!hasPromotion()) {
            return Quantity.ZERO;
        }
        return ((PromotionProduct) product).calculateFreeItems(quantity);
    }

}