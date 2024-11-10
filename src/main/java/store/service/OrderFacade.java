package store.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.discount.DiscountManager;
import store.domain.order.Order;
import store.domain.vo.Quantity;
import store.dto.request.OrderRequestDto;
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

    public OrderResponseDto processOrder(List<OrderRequestDto> orderRequests, boolean hasMembership) {
        Map<String, Quantity> items = convertToItemMap(orderRequests);
        Order order = orderService.createOrder(items, hasMembership);
        discountManager.applyDiscount(order);
        return OrderResponseDto.from(order);
    }

    private Map<String, Quantity> convertToItemMap(List<OrderRequestDto> orderRequests) {
        return orderRequests.stream()
                .collect(Collectors.toMap(
                        OrderRequestDto::productName,
                        OrderRequestDto::quantity
                ));
    }
}