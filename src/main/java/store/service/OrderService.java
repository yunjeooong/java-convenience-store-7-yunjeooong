package store.service;

import java.util.Map;
import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.product.Product;
import store.domain.vo.Quantity;
import java.util.List;
import store.repository.ProductRepository;

public class OrderService {
    private final ProductRepository productRepository;

    private static final String ERROR_INSUFFICIENT_STOCK = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final String ERROR_PRODUCT_NOT_FOUND = "[ERROR] 존재하지 않는 상품입니다: %s";

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Order createOrder(Map<String, Quantity> items, boolean hasMembership) {
        validateItems(items);
        Order order = Order.create();
        for (Map.Entry<String, Quantity> entry : items.entrySet()) {
            Product product = findProduct(entry.getKey());
            order.addLineItem(product, entry.getValue());
        }
        order.removeStocks();
        updateProducts(order.getLineItems());
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
            throw new IllegalArgumentException(ERROR_INSUFFICIENT_STOCK);
        }
    }

    private void updateProducts(List<OrderLineItem> lineItems) {
        for (OrderLineItem item : lineItems) {
            Product product = findProduct(item.getProductName());
            productRepository.updateProduct(product);
        }
    }

    private Product findProduct(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> createProductNotFoundException(name));
    }

    private IllegalArgumentException createProductNotFoundException(String name) {
        return new IllegalArgumentException(String.format(ERROR_PRODUCT_NOT_FOUND, name));
    }
}