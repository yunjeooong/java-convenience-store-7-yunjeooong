package store.service;

import store.domain.discount.DiscountManager;
import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.vo.Money;
import store.domain.product.Product;
import store.domain.vo.Quantity;
import store.dto.request.OrderRequestDto;

import java.util.List;
import java.util.stream.Collectors;
import store.repository.ProductRepository;

public class OrderService {
    private final DiscountManager discountManager;
    private final ProductRepository productRepository;

    public OrderService(DiscountManager discountManager, ProductRepository productRepository) {
        this.discountManager = discountManager;
        this.productRepository = productRepository;
    }

    public Order createOrder(List<OrderRequestDto> orderItems, boolean hasMembership) {
        List<OrderLineItem> lineItems = convertToOrderLineItems(orderItems);
        return createOrderWithLineItems(lineItems, hasMembership);
    }

    private List<OrderLineItem> convertToOrderLineItems(List<OrderRequestDto> orderItems) {
        return orderItems.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderRequestDto item) {
        Product product = findProductByName(item.productName());
        return OrderLineItem.create(product, item.quantity());
    }

    private Product findProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
    }

    private Order createOrderWithLineItems(List<OrderLineItem> lineItems, boolean hasMembership) {
        return Order.create(lineItems, hasMembership);
    }

    public Money calculateTotalAmount(Order order) {
        return order.calculateTotalAmount();
    }

    public Money calculateDiscount(Order order) {
        return discountManager.calculateTotalDiscount(order);
    }
}