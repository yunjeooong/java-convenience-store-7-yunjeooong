package store.controller;

import java.util.Map;
import store.domain.order.Order;
import store.domain.order.OrderLineItem;
import store.domain.discount.DiscountManager;
import store.domain.vo.Money;
import store.domain.vo.Quantity;
import store.dto.request.OrderRequestDto;
import store.dto.response.OrderResponseDto;
import store.service.OrderFacade;
import store.service.OrderService;
import store.service.ReceiptService;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.stream.Collectors;

public class OrderController {
    private final InputView inputView;
    private final OrderFacade orderFacade;
    private final ReceiptService receiptService;

    public OrderController(
            InputView inputView,
            OrderFacade orderFacade,
            ReceiptService receiptService) {
        this.inputView = inputView;
        this.orderFacade = orderFacade;
        this.receiptService = receiptService;
    }

    public void processOrder() {
        Map<String, Quantity> items = inputView.readItems();
        boolean hasMembership = inputView.readMembershipChoice();

        OrderResponseDto orderResponse = orderFacade.processOrder(items, hasMembership);
        receiptService.printReceipt(orderResponse);
    }
}