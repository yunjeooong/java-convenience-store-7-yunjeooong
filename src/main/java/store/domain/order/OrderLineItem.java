package store.domain.order;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Money;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class OrderLineItem {
    private final Product product;
    private final Quantity quantity;

    private OrderLineItem(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderLineItem create(Product product, Quantity quantity) {
        return new OrderLineItem(product, quantity);
    }

    public void removeStock() {
        product.removeStock(quantity);
    }

    public String productName() {
        return product.getName();
    }

    public int quantityValue() {
        return quantity.value();
    }

    public Money calculateItemPrice() {
        return new Money(product.calculateTotalPrice(quantity).value());
    }

    public boolean hasPromotion() {
        return product.isPromotionProduct();
    }

    public Quantity calculateFreeQuantity() {
        if (!hasPromotion()) {
            return Quantity.ZERO;
        }
        return ((PromotionProduct) product).calculateFreeItems(quantity);
    }

    public OrderLineItem createFreeItemLine() {
        Quantity freeQuantity = calculateFreeQuantity();
        if (freeQuantity.value() == 0) {
            return null;
        }
        return OrderLineItem.create(product, freeQuantity);
    }

    public Price getPrice() {
        return new Price(calculateItemPrice().value());
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public int getTotalQuantity() {
        return quantity.value() + calculateFreeQuantity().value();
    }
}