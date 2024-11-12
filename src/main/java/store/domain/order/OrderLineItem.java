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
        validateStock(product, quantity);
        return new OrderLineItem(product, quantity);
    }

    private static void validateStock(Product product, Quantity quantity) {
        if (product instanceof PromotionProduct) {
            Quantity totalQuantity = quantity.add(
                    product.getPromotionType().calculateFreeItems(quantity)
            );
            product.validateStock(totalQuantity);
        } else {
            product.validateStock(quantity);
        }
    }

    public void removeStock() {
        if (product instanceof PromotionProduct) {
            product.removeStock(calculateTotalQuantity());
        } else {
            product.removeStock(quantity);
        }
    }

    private Quantity calculateTotalQuantity() {
        return quantity.add(calculateFreeQuantity());
    }

    public String getProductName() {
        return product.getName();
    }

    public Price getPrice() {
        return product.getPrice();
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
        return product.getPromotionType().calculateFreeItems(quantity);
    }

    public OrderLineItem createFreeItemLine() {
        Quantity freeQuantity = calculateFreeQuantity();
        if (freeQuantity.value() == 0) {
            return null;
        }
        return OrderLineItem.create(product, freeQuantity);
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}