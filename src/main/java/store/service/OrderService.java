package store.service;

import java.util.Map;
import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.product.Product;
import store.domain.product.PromotionProduct;
import store.domain.vo.Quantity;
import java.util.List;
import java.util.stream.Collectors;
import store.repository.ProductRepository;

public class OrderService {
    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Order createOrder(Map<String, Quantity> items, boolean hasMembership) {
        Order order = Order.create();
        for (Map.Entry<String, Quantity> entry : items.entrySet()) {
            Product product = findProduct(entry.getKey());
            order.addLineItem(product, entry.getValue());
        }

        order.removeStocks();
        productRepository.saveCurrentState();

        return order;
    }

    private void validateItems(Map<String, Quantity> items) {
        items.forEach(this::validateItem);
    }

    private void validateItem(String name, Quantity quantity) {
        Product product = findProduct(name);
        validateStock(product, quantity);
    }

    private void validateStock(Product product, Quantity quantity) {
        if (!product.hasEnoughStock(quantity)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private List<OrderLineItem> createOrderItems(Map<String, Quantity> items) {
        return items.entrySet().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderItem(Map.Entry<String, Quantity> entry) {
        Product product = findProduct(entry.getKey());
        Quantity finalQuantity = calculateFinalQuantity(product, entry.getValue());
        return OrderLineItem.create(product, finalQuantity);
    }

    private Quantity calculateFinalQuantity(Product product, Quantity quantity) {
        if (!product.isPromotionProduct()) {
            return quantity;
        }
        return quantity;
    }

    private Quantity addPromotionQuantity(PromotionProduct product, Quantity quantity) {
        if (!product.canApplyPromotion(quantity)) {
            return quantity;
        }
        return quantity.add(product.calculateFreeItems(quantity));
    }

    private void updateStocks(List<OrderLineItem> orderItems) {
        orderItems.forEach(OrderLineItem::removeStock);
    }

    private Product findProduct(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> createProductNotFoundException(name));
    }

    private IllegalArgumentException createProductNotFoundException(String name) {
        return new IllegalArgumentException(
                String.format("[ERROR] 존재하지 않는 상품입니다: %s", name));
    }
}