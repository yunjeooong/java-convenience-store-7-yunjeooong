package store.domain.order;

import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Money;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class OrderLineItem {
    private final Product product;
    private final Quantity quantity;
    private final Price price;

    private OrderLineItem(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = calculatePrice(product, quantity);
    }

    public static OrderLineItem create(Product product, Quantity quantity) {
        return new OrderLineItem(product, quantity);
    }

    private Price calculatePrice(Product product, Quantity quantity) {
        return product.calculateTotalPrice(quantity);
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
        return price.toMoney();
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
        return OrderLineItem.create(product, calculateFreeQuantity());
    }

    public Price getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}