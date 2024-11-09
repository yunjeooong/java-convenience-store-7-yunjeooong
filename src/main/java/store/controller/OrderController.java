package store.controller;

import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.discount.DiscountManager;
import store.domain.vo.Money;
import store.dto.request.OrderRequestDto;
import store.service.OrderService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.stream.Collectors;

public class OrderController {
    private final InputView inputView;
    private final OutputView outputView;
    private final OrderService orderService;
    private final DiscountManager discountManager;

    public OrderController(InputView inputView, OutputView outputView, OrderService orderService, DiscountManager discountManager) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.orderService = orderService;
        this.discountManager = discountManager;
    }

    public void processOrder() {
        Order order = createOrder();
        Money totalDiscount = discountManager.calculateTotalDiscount(order);
        order.applyDiscount(totalDiscount);
    }

    private Order createOrder() {
        List<OrderRequestDto> orderItems = inputView.readItems().entrySet().stream()
                .map(entry -> new OrderRequestDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        boolean hasMembership = inputView.readMembershipChoice();
        return orderService.createOrder(orderItems, hasMembership);
    }

}
