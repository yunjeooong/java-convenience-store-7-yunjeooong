package store.service;

import java.util.Map;
import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.product.RegularProduct;
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
        validateItems(items);
        List<OrderLineItem> orderItems = createOrderItems(items);
        return Order.create(orderItems, hasMembership);
    }

    private void validateItems(Map<String, Quantity> items) {
        items.forEach((name, quantity) -> {
            RegularProduct product = findProduct(name);
            if (!product.hasEnoughStock(quantity)) {
                throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            }
        });
    }

    private List<OrderLineItem> createOrderItems(Map<String, Quantity> items) {
        return items.entrySet().stream()
                .map(entry -> OrderLineItem.create(
                        findProduct(entry.getKey()),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }

    private RegularProduct findProduct(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("[ERROR] 존재하지 않는 상품입니다: %s", name)));
    }
}