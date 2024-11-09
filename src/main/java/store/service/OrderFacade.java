package store.service;

import java.util.Map;
import store.domain.discount.DiscountManager;
import store.domain.order.Order;
import store.domain.vo.Quantity;
import store.dto.response.OrderResponseDto;

public class OrderFacade {
    private final OrderService orderService;
    private final DiscountManager discountManager;

    public OrderFacade(
            OrderService orderService,
            DiscountManager discountManager) {
        this.orderService = orderService;
        this.discountManager = discountManager;
    }

    public OrderResponseDto processOrder(Map<String, Quantity> items, boolean hasMembership) {
        Order order = orderService.createOrder(items, hasMembership);
        discountManager.applyDiscount(order);
        return OrderResponseDto.from(order);
    }
}
